package com.geoffwellington.manager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.geoffwellington.manager.model.dto.ManagedListDTO;
import com.geoffwellington.manager.service.ManagedListService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ManagedListControllerTest {

    @Mock
    private ManagedListService managedListService;

    private ManagedListController managedListController;

    @BeforeEach
    public void setup() {
        managedListController = new ManagedListController(managedListService);
    }

    @Test
    public void shouldReturnNewListAnd201WhenBodyValid() {
        ManagedListDTO anyValidDTO = new ManagedListDTO();
        when(managedListService.createList(anyValidDTO)).thenReturn(new ManagedListDTO());

        ResponseEntity<ManagedListDTO> response = managedListController.createManagedList(anyValidDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldUpdateExistingListAndReturn200WhenIdAndBodyValid() {
        ManagedListDTO anyValidDTO = new ManagedListDTO();
        UUID anyMatchingID = UUID.randomUUID();
        when(managedListService.updateList(anyMatchingID, anyValidDTO)).thenReturn(new ManagedListDTO());

        ResponseEntity<ManagedListDTO> response = managedListController.updatedManagedList(anyMatchingID, anyValidDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void shouldDeleteAndReturn200WhenIdValid() {
        UUID anyMatchingID = UUID.randomUUID();

        ResponseEntity<ManagedListDTO> response = managedListController.deleteManagedList(anyMatchingID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldReturnListAnd200WhenIdValid() {
        ManagedListDTO anyValidDTO = new ManagedListDTO();
        UUID anyMatchingID = UUID.randomUUID();
        when(managedListService.getManagedList(anyMatchingID)).thenReturn(anyValidDTO);

        ResponseEntity<ManagedListDTO> response = managedListController.getManagedList(anyMatchingID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}