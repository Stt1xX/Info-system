package com.example.backend.servicies;

import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.entities.DTO.HumanDTO;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {

    private static final String[] sheetNames = new String[]{"Cars", "Coordinates", "Humans"};
    private static final List<String> ArchiveExtensions = List.of("zip", "rar", "7z");
    private static final List<String> fileExtensions = List.of("xlsx");

    private final CarService carService;
    private final CoordinatesService coordinatesService;
    private final HumanService humanService;

    public FileService(CarService carService, CoordinatesService coordinatesService, HumanService humanService) {
        this.carService = carService;
        this.coordinatesService = coordinatesService;
        this.humanService = humanService;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<?> mainImport(MultipartFile file){
        if (ArchiveExtensions.contains(getFileExtension(file))) {
            return importArchive(file);
        } else if (fileExtensions.contains(getFileExtension(file))) {
            return importFile(file);
        } else {
            return ResponseEntity.badRequest().body("Incorrect file's extension");
        }
    }

    private ResponseEntity<?> importArchive(MultipartFile file) {
        return ResponseEntity.ok("It's Archive");
    }

    private ResponseEntity<?> importFile(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty!");
        }

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            ResponseEntity<?> resp = carService.addAll(getCars(workbook));
            if (resp.getStatusCode().isError()) {
                return resp;
            }
            resp = coordinatesService.addAll(getCoordinates(workbook));
            if (resp.getStatusCode().isError()) {
                return resp;
            }
            resp = humanService.addAll(getHumans(workbook));
            if (resp.getStatusCode().isError()) {
                return resp;
            }
            return ResponseEntity.ok("The file has been processed successfully!");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File processing error");
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

}
