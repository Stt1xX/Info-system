package com.example.backend.controllers;

import com.example.backend.entities.Anntotations.SearchableField;
import com.example.backend.entities.Anntotations.SortableField;
import com.example.backend.entities.DTO.PageRequestDTO;
import com.example.backend.entities.DTO.FieldsDTO;
import com.example.backend.servicies.ItemService;
import com.example.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
public abstract class ItemController<ClassDTO, MainClass> {

    protected final ItemService<ClassDTO, MainClass> itemService;
    protected final Class<MainClass> mainClass;

    @Autowired
    public ItemController(ItemService<ClassDTO, MainClass> itemService, Class<MainClass> mainClass) {
        this.itemService = itemService;
        this.mainClass = mainClass;
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ClassDTO classDTO) {
        ResponseEntity<?> resp = itemService.add(classDTO);
        if (resp.getStatusCode() != HttpStatus.OK){
            return resp;
        } else {
            return ResponseEntity.ok().body(((List<?>)(Objects.requireNonNull(resp.getBody()))).get(0));
        }
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

    @GetMapping("/get_commits/{id}/{pageNumber}")
    public ResponseEntity<?> getCommits(@PathVariable Integer id, @PathVariable Integer pageNumber) {
        return itemService.getCommits(id, pageNumber);
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
