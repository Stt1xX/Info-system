package com.example.backend.controllers;

import com.example.backend.entities.Anntotations.SearchableField;
import com.example.backend.entities.Anntotations.SortableField;
import com.example.backend.entities.DTO.PageRequestDTO;
import com.example.backend.entities.DTO.FieldsDTO;
import com.example.backend.servicies.ItemService;
import com.example.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public abstract class ItemController<ClassDTO, MainClass> {

    private final ItemService<ClassDTO, MainClass> itemService;
    private final Class<MainClass> mainClass;

    @Autowired
    public ItemController(ItemService<ClassDTO, MainClass> itemService, Class<MainClass> mainClass) {
        this.itemService = itemService;
        this.mainClass = mainClass;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ClassDTO classDTO) {
        return itemService.add(classDTO);
    }

    @GetMapping("/get_all")
    public ResponseEntity<?> getAll(@ModelAttribute PageRequestDTO pageRequestDTO) {
        return itemService.getAll(pageRequestDTO);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ClassDTO classDTO) {
        return itemService.update(id, classDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return itemService.delete(id);
    }

    @GetMapping("/get_sortable_fields")
    public List<FieldsDTO> getSortableFields() {
        return Utils.getFields(mainClass, SortableField.class);
    }

    @GetMapping("/get_searchable_fields")
    public List<FieldsDTO> getSearchableFields() {
        return Utils.getFields(mainClass, SearchableField.class);
    }
}
