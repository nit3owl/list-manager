package com.geoffwellington.manager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.geoffwellington.manager.exception.ListNotFoundException;
import com.geoffwellington.manager.exception.UserNotFoundException;
import com.geoffwellington.manager.exception.ValidationException;
import com.geoffwellington.manager.model.ListItem;
import com.geoffwellington.manager.model.ManagedList;
import com.geoffwellington.manager.model.dto.ListItemDTO;
import com.geoffwellington.manager.model.dto.ManagedListDTO;
import com.geoffwellington.manager.model.dto.UserDTO;
import com.geoffwellington.manager.repository.ManagedListRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class ManagedListServiceV1Test {

    @Mock
    private ManagedListRepository managedListRepository;

    @Mock
    private UserService userService;

    @Spy
    private ModelMapper modelMapper;

    private ManagedListService managedListService;

    @BeforeEach
    public void setup() {
        managedListService = new ManagedListServiceV1(managedListRepository, userService, modelMapper);
    }

    @Test
    public void shouldCreateNewListWithEmptyItems() {
        ManagedListDTO anyNewList = new ManagedListDTO();
        UUID anyValidUser = UUID.randomUUID();
        anyNewList.setOwnerId(anyValidUser);
        ManagedList list = new ManagedList();

        when(userService.getUser(anyValidUser)).thenReturn(new UserDTO());
        when(managedListRepository.save(list)).thenReturn(list);
        when(modelMapper.map(anyNewList, ManagedList.class)).thenReturn(list);
        when(modelMapper.map(list, ManagedListDTO.class)).thenReturn(new ManagedListDTO());

        ManagedListDTO created = managedListService.createList(anyNewList);
        assertNotNull(created);
    }

    @Test
    public void shouldThrowExceptionWhenOwnerNotFound() {
        ManagedListDTO anyNewList = new ManagedListDTO();
        UUID anyInvalidUser = UUID.randomUUID();
        anyNewList.setOwnerId(anyInvalidUser);

        when(userService.getUser(anyInvalidUser)).thenThrow(new UserNotFoundException("Not found."));

        assertThrows(ValidationException.class, () -> managedListService.createList(anyNewList),
                "Expected ValidationException");
    }

    @Test
    public void shouldRetrieveExistingListWithValidId() {
        UUID anyExistingListId = UUID.randomUUID();
        ManagedList anyExistingList = new ManagedList();

        when(managedListRepository.findById(anyExistingListId)).thenReturn(Optional.of(anyExistingList));
        when(modelMapper.map(anyExistingList, ManagedListDTO.class)).thenReturn(new ManagedListDTO());

        ManagedListDTO managedList = managedListService.getManagedList(anyExistingListId);
        assertNotNull(managedList);
    }

    @Test
    public void shouldThrowExceptionWhenListIdNotFound() {
        UUID anyNonExistentId = UUID.randomUUID();

        when(managedListRepository.findById(anyNonExistentId)).thenReturn(Optional.empty());

        assertThrows(ListNotFoundException.class, () -> managedListService.getManagedList(anyNonExistentId),
                "Expected ListNotFoundException");
    }

    @Test
    public void shouldDeleteExistingList() {
        UUID anyExistingListId = UUID.randomUUID();

        managedListService.deleteList(anyExistingListId);
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistentList() {
        UUID anyNonExistentListId = UUID.randomUUID();
        ManagedListDTO anyList = new ManagedListDTO();

        when(managedListRepository.findById(anyNonExistentListId)).thenReturn(Optional.empty());

        assertThrows(ListNotFoundException.class, () -> managedListService.updateList(anyNonExistentListId, anyList),
                "Expected ListNotFoundException");
    }

    @Test
    public void shouldAddNewItemsToExistingList() {
        UUID anyExistingListId = UUID.randomUUID();

        ManagedListDTO anyUpdatedExistingListDTO = new ManagedListDTO();
        anyUpdatedExistingListDTO.setId(anyExistingListId);
        ListItemDTO existingItemDto = new ListItemDTO("existing", 1);
        ListItemDTO newItemDto = new ListItemDTO("new", 2);
        anyUpdatedExistingListDTO.setItems(Set.of(existingItemDto, newItemDto));

        ManagedList convertedList = new ManagedList();
        convertedList.setId(anyExistingListId);
        ListItem convertedExistingItem = new ListItem(UUID.randomUUID(), convertedList, "existing", "existing", 1);
        ListItem convertedNewItem = new ListItem(UUID.randomUUID(), convertedList, "new", "new", 2);
        convertedList.setItems(Sets.newHashSet(List.of(convertedExistingItem, convertedNewItem)));

        ManagedList existingList = new ManagedList();
        existingList.setId(anyExistingListId);
        ListItem existingItem = new ListItem(UUID.randomUUID(), existingList, "existing", "existing", 1);
        existingList.setItems(Sets.newHashSet(List.of(existingItem)));

        when(managedListRepository.findById(anyExistingListId)).thenReturn(Optional.of(existingList));
        when(modelMapper.map(anyUpdatedExistingListDTO, ManagedList.class)).thenReturn(convertedList);
        when(managedListRepository.save(existingList)).thenReturn(existingList);
        // Call real method in order to verify the actual list contents
        when(modelMapper.map(existingList, ManagedListDTO.class)).thenCallRealMethod();


        ManagedListDTO managedList = managedListService.updateList(anyExistingListId, anyUpdatedExistingListDTO);
        assertNotNull(managedList);
        assertEquals(2, managedList.getItems().size());
        assertTrue(managedList.getItems().contains(existingItemDto));
        assertTrue(managedList.getItems().contains(newItemDto));
    }

    @Test
    public void shouldRemoveItemsFromExistingList() {
        UUID anyExistingListId = UUID.randomUUID();

        ManagedListDTO anyUpdatedExistingListDTO = new ManagedListDTO();
        anyUpdatedExistingListDTO.setId(anyExistingListId);
        ListItemDTO existingItemDto = new ListItemDTO("existing", 1);
        anyUpdatedExistingListDTO.setItems(Set.of(existingItemDto));

        ManagedList convertedList = new ManagedList();
        convertedList.setId(anyExistingListId);
        ListItem convertedExistingItem = new ListItem(UUID.randomUUID(), convertedList, "existing", "existing", 1);
        convertedList.setItems(Sets.newHashSet(List.of(convertedExistingItem)));

        ManagedList existingList = new ManagedList();
        existingList.setId(anyExistingListId);
        ListItem existingItem = new ListItem(UUID.randomUUID(), existingList, "existing", "existing", 1);
        ListItem existingItemToRemove = new ListItem(UUID.randomUUID(), existingList, "otherExisting", "otherExisting", 1);
        existingList.setItems(Sets.newHashSet(List.of(existingItem, existingItemToRemove)));

        when(managedListRepository.findById(anyExistingListId)).thenReturn(Optional.of(existingList));
        when(modelMapper.map(anyUpdatedExistingListDTO, ManagedList.class)).thenReturn(convertedList);
        when(managedListRepository.save(existingList)).thenReturn(existingList);
        // Call real method in order to verify the actual list contents
        when(modelMapper.map(existingList, ManagedListDTO.class)).thenCallRealMethod();


        ManagedListDTO managedList = managedListService.updateList(anyExistingListId, anyUpdatedExistingListDTO);
        assertNotNull(managedList);
        assertEquals(1, managedList.getItems().size());
        assertTrue(managedList.getItems().contains(existingItemDto));
    }
}