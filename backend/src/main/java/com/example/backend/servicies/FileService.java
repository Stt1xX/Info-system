package com.example.backend.servicies;

import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.entities.DTO.HumanDTO;
import com.example.backend.servicies.enums.CounterIndex;
import com.example.backend.utils.ArchiveProcessor;
import com.github.junrar.exception.RarException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class FileService {

    private static final String[] sheetNames = new String[]{"Cars", "Coordinates", "Humans"};
    private static final List<String> ArchiveExtensions = List.of("zip", "rar", "7z");
    private static final List<String> fileExtensions = List.of("xlsx");

    private final CarService carService;
    private final CoordinatesService coordinatesService;
    private final HumanService humanService;
    private final MinioService minioService;

    public static final int MAX_RETRY_COUNT = ArchiveProcessor.THREAD_POOL_SIZE;

    @Lazy
    @Autowired
    private FileService self;

    public FileService(CarService carService, CoordinatesService coordinatesService, HumanService humanService, MinioService minioService) {
        this.carService = carService;
        this.coordinatesService = coordinatesService;
        this.humanService = humanService;
        this.minioService = minioService;
    }

    public ResponseEntity<?> mainImport(MultipartFile file) throws IOException {
        File temp_file = convertMultipartFileToFile(file);
        ResponseEntity<?> resp;
        if (ArchiveExtensions.contains(getFileExtension(file))) {
            resp = importArchive(temp_file);
        } else if (fileExtensions.contains(getFileExtension(file))) {
            resp = self.importFile(temp_file);
        } else {
            return ResponseEntity.badRequest().body("Incorrect file's extension");
        }
        //noinspection ResultOfMethodCallIgnored
        temp_file.delete();
        return resp;
    }


    public ResponseEntity<?> importArchive(File archiveFile) {

        List<File> extractedFiles;
        try {
             extractedFiles = ArchiveProcessor.extractFiles(archiveFile);
        } catch (IOException | RarException e) {
            return ResponseEntity.badRequest().body("Error during archive parsing");
        }
        int MAX_FILES_ARCHIVE = 30;
        if (extractedFiles.size() > MAX_FILES_ARCHIVE) {
            return ResponseEntity.badRequest().body("Too many files in archive");
        }
        ExecutorService executor = Executors.newFixedThreadPool(ArchiveProcessor.THREAD_POOL_SIZE);
        List<Future<ResponseEntity<?>>> futures = new ArrayList<>();

        for (File file : extractedFiles) {
            futures.add(executor.submit(() -> self.importFile(file)));
        }

        int successFiles = 0;
        for (Future<ResponseEntity<?>> future : futures) {
            try {
                if (future.get().getStatusCode() == HttpStatus.OK) {
                    successFiles++;
                }
            } catch (LockAcquisitionException | InterruptedException | ExecutionException e){
                // ignoring
            }
        }
        executor.shutdown();

        return ResponseEntity.ok("Processed " + successFiles + " files from archive");
    }

    @Retryable(maxAttempts = MAX_RETRY_COUNT, listeners = "transactionListener")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<?> importFile(File file) {
        if (file == null || !file.exists() || file.length() == 0) {
            return ResponseEntity.badRequest().body("File is empty or does not exist!");
        }
        try (InputStream inputStream = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            ResponseEntity<?> resp = carService.addAll(getCars(workbook));
            int[] cars_num, coords_num, humans_num;
            if (resp.getStatusCode() != HttpStatus.OK) {
                throw new DataIntegrityViolationException((String) resp.getBody());
            } else {
                cars_num = (int[]) resp.getBody();
            }
            resp = coordinatesService.addAll(getCoordinates(workbook));
            if (resp.getStatusCode() != HttpStatus.OK) {
                throw new DataIntegrityViolationException((String) resp.getBody());
            } else {
                coords_num = (int[]) resp.getBody();
            }
            resp = humanService.addAll(getHumans(workbook));
            if (resp.getStatusCode() != HttpStatus.OK) {
                throw new DataIntegrityViolationException((String) resp.getBody());
            } else {
                humans_num = (int[]) resp.getBody();
            }
            RetryContext context = RetrySynchronizationManager.getContext();
            int humans_num_sum = humans_num != null ? humans_num[CounterIndex.HUMAN.getValue()] : 0,
                    cars_num_sum = (cars_num != null ? cars_num[CounterIndex.CAR.getValue()] : 0)
                            + (humans_num != null ? humans_num[CounterIndex.CAR.getValue()] : 0),
                    coords_num_sum = (coords_num != null ? coords_num[CounterIndex.COORDINATES.getValue()] : 0) +
                            (humans_num != null ? humans_num[CounterIndex.COORDINATES.getValue()] : 0);

            String fileName = minioService.uploadFile(file);

            context.setAttribute("humans", humans_num_sum);
            context.setAttribute("cars", cars_num_sum);
            context.setAttribute("coords", coords_num_sum);
            context.setAttribute("fileName", fileName);

            return ResponseEntity.ok("The file has been processed successfully!");
        } catch (IOException e) {
            throw new DataIntegrityViolationException("File processing error");
        }
    }


    private Map<Integer, CarDTO> getCars(Workbook workbook) throws IllegalStateException {
        Sheet sheet = workbook.getSheet(sheetNames[0]);
        DataFormatter formatter = new DataFormatter();
        if (sheet == null) {
            return new HashMap<>();
        }
        Map<Integer, CarDTO> cars = new HashMap<>();
        for (Row row : sheet) {
            try{
                CarDTO car = new CarDTO();
                car.setName(formatter.formatCellValue(row.getCell(0)));
                if (row.getCell(1) == null)
                    car.setCool(false);
                else
                    car.setCool(row.getCell(1).getBooleanCellValue());
                cars.put(row.getRowNum() + 1, car);
            } catch (IllegalStateException e) {
                throw new DataIntegrityViolationException("Incorrect file format: Cars: Line " +
                        (row.getRowNum() + 1) + ": " + e.getMessage());
            } catch (NullPointerException e){
                throw new DataIntegrityViolationException("Incorrect file format: Cars: Line " +
                        (row.getRowNum() + 1) + ": " + "Fields can't be empty");
            }
        }
        return cars;
    }

    private Map<Integer, CoordinatesDTO> getCoordinates(Workbook workbook) throws IllegalStateException {
        Sheet sheet = workbook.getSheet(sheetNames[1]);
        if (sheet == null) {
            return new HashMap<>();
        }
        Map<Integer, CoordinatesDTO> coordinates = new HashMap<>();
        for (Row row : sheet) {
            try {
                CoordinatesDTO coordinate = new CoordinatesDTO();
                coordinate.setX((int) row.getCell(0).getNumericCellValue());
                coordinate.setY((int) row.getCell(1).getNumericCellValue());
                coordinates.put(row.getRowNum() + 1, coordinate);
            } catch (IllegalStateException e) {
                throw new DataIntegrityViolationException("Incorrect file format: Coordinates: Line " +
                        (row.getRowNum() + 1) + ": " + e.getMessage());
            } catch (NullPointerException e) {
                throw new DataIntegrityViolationException("Incorrect file format: Coordinates: Line " +
                        (row.getRowNum() + 1) + ": " + "Fields can't be empty");
            }
        }
        return coordinates;
    }

    private Map<Integer, HumanDTO> getHumans(Workbook workbook) throws IllegalStateException {
        Sheet sheet = workbook.getSheet(sheetNames[2]);
        if (sheet == null) {
            return new HashMap<>();
        }
        Map<Integer, HumanDTO> humans = new HashMap<>();
        for (Row row : sheet) {
            try {
            HumanDTO human = new HumanDTO();
            human.setName(row.getCell(0).getStringCellValue());
            human.setSoundtrackName(row.getCell(1).getStringCellValue());
            human.setImpactSpeed((int) row.getCell(2).getNumericCellValue());
            human.setMinutesOfWaiting((long) row.getCell(3).getNumericCellValue());
            human.setWeaponType(row.getCell(4).getStringCellValue());
            human.setMood(row.getCell(5).getStringCellValue());
            if (row.getCell(6) != null)
                human.setRealHero(row.getCell(6).getBooleanCellValue());
            else
                human.setRealHero(false);
            if (row.getCell(7) != null)
                human.setHasToothpick(row.getCell(7).getBooleanCellValue());
            else
                human.setHasToothpick(false);
            if (row.getCell(8) != null) {
                human.setCarId((int) row.getCell(8).getNumericCellValue());
            } else {
                human.setCarName(row.getCell(10).getStringCellValue());
                human.setCarIsCool(row.getCell(11).getBooleanCellValue());
            }
            if (row.getCell(9) != null) {
                human.setCoordinatesId((int) row.getCell(9).getNumericCellValue());
            } else {
                human.setX(row.getCell(12).getNumericCellValue());
                human.setY((float) row.getCell(13).getNumericCellValue());
            }
            humans.put(row.getRowNum(), human);
            } catch (IllegalStateException e) {
                throw new DataIntegrityViolationException("Incorrect file format: Humans: Line " +
                        (row.getRowNum() + 1) + ": " + e.getMessage());
            } catch (NullPointerException e) {
                throw new DataIntegrityViolationException("Incorrect file format: Humans: Line " +
                        (row.getRowNum() + 1) + ": " + "Fields can't be empty");
            }
        }
        return humans;
    }

    private static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        return "";
    }

    private static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
}

@Component
class TransactionListener implements RetryListener {

    private final ImportRecordService importRecordService;
    private final MinioService minioService;

    public TransactionListener(ImportRecordService importRecordService, MinioService minioService) {
        this.importRecordService = importRecordService;
        this.minioService = minioService;
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        if (context.getRetryCount() == FileService.MAX_RETRY_COUNT) {
            importRecordService.createFailedRecord();
        }
        try {
            String fileName = (String) context.getAttribute("fileName");
            if (fileName != null) {
                minioService.deleteFile(fileName);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        if (!context.hasAttribute(RetryContext.EXHAUSTED)) {
            Integer cars = (Integer) context.getAttribute("cars");
            Integer humans = (Integer) context.getAttribute("humans");
            Integer coords = (Integer) context.getAttribute("coords");
            String fileName = (String) context.getAttribute("fileName");
            if (cars != null && humans != null && coords != null && fileName != null) {
                importRecordService.createSuccessRecord(cars, humans, coords, fileName);
            }
        }
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
        return true;
    }
}
