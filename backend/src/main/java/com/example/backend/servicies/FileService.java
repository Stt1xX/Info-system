package com.example.backend.servicies;

import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.entities.DTO.CoordinatesDTO;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    public FileService(CarService carService, CoordinatesService coordinatesService) {
        this.carService = carService;
        this.coordinatesService = coordinatesService;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<?> mainImport(MultipartFile file){
        if (ArchiveExtensions.contains(getFileExtension(file))) {
            return importArchive(file);
        } else if (fileExtensions.contains(getFileExtension(file))) {
            return importFile(file);
        } else {
            return ResponseEntity.badRequest().body("Incorrect file format");
        }
    }

    protected ResponseEntity<?> importArchive(MultipartFile file) {
        return ResponseEntity.ok("It's Archive");
    }

    protected ResponseEntity<?> importFile(MultipartFile file) {
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
            return ResponseEntity.ok("The file has been processed successfully!");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File processing error");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Incorrect file format");
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
            CarDTO car = new CarDTO();
            if(row.getCell(1) == null){
                continue;
            }
            car.setName(formatter.formatCellValue(row.getCell(0)));
            car.setCool(row.getCell(1).getBooleanCellValue());
            cars.put(row.getRowNum(), car);
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
            CoordinatesDTO coordinate = new CoordinatesDTO();
            coordinate.setX((int) row.getCell(0).getNumericCellValue());
            coordinate.setY((int) row.getCell(1).getNumericCellValue());
            coordinates.put(row.getRowNum(), coordinate);
        }
        return coordinates;
    }

    private static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        return "";
    }

}
