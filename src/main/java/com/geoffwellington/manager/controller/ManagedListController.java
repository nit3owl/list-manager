package com.geoffwellington.manager.controller;

import com.geoffwellington.manager.model.dto.ManagedListDTO;
import com.geoffwellington.manager.service.ManagedListService;
import java.lang.invoke.MethodHandles;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lists")
public class ManagedListController {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ManagedListService managedListService;

    public ManagedListController(ManagedListService managedListService) {
        this.managedListService = managedListService;
    }

    @PostMapping
    public ResponseEntity<ManagedListDTO> createManagedList(@RequestBody ManagedListDTO listRequest) {
        ManagedListDTO listDto = managedListService.createList(listRequest);

        return new ResponseEntity<>(listDto, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ManagedListDTO> getManagedList(@PathVariable UUID id) {
        ManagedListDTO listDto = managedListService.getManagedList(id);

        return new ResponseEntity<>(listDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ManagedListDTO> updatedManagedList(@PathVariable UUID id,
                                                             @RequestBody ManagedListDTO listRequest) {
        ManagedListDTO listDto = managedListService.updateList(id, listRequest);

        return new ResponseEntity<>(listDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ManagedListDTO> deleteManagedList(@PathVariable UUID id) {
        managedListService.deleteList(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
