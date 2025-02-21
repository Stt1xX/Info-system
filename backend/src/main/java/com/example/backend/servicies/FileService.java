package com.example.backend.servicies;

import com.example.backend.entities.DTO.CarDTO;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private static final String[] sheetNames = new String[]{"Cars", "Coordinates", "Humans"};

    private final CarService carService;

    public FileService(CarService carService) {
        this.carService = carService;
    }

    public ResponseEntity<?> importFile(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty!");
        }

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            return carService.addAll(getCars(workbook));
        }
        catch (IOException e) {
                return ResponseEntity.badRequest().body("Ошибка обработки файла: " + e.getMessage());
        }
    }

    private List<CarDTO> getCars(Workbook workbook){
        Sheet sheet = workbook.getSheet(sheetNames[0]);
        DataFormatter formatter = new DataFormatter();
        if (sheet == null) {
            return new ArrayList<>();
        }
        List<CarDTO> cars = new ArrayList<>();
        for (Row row : sheet) {
            CarDTO car = new CarDTO();
            car.setName(formatter.formatCellValue(row.getCell(0)));
            car.setCool(row.getCell(1).getBooleanCellValue());
            cars.add(car);
        }
        return cars;
    }

}
