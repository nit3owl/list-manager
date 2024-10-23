package com.geoffwellington.manager.service;

import com.geoffwellington.manager.exception.ListNotFoundException;
import com.geoffwellington.manager.exception.UserNotFoundException;
import com.geoffwellington.manager.exception.ValidationException;
import com.geoffwellington.manager.model.ListItem;
import com.geoffwellington.manager.model.ManagedList;
import com.geoffwellington.manager.model.dto.ManagedListDTO;
import com.geoffwellington.manager.repository.ManagedListRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;

public class ManagedListServiceV1 implements ManagedListService {

    private final ManagedListRepository managedListRepository;

    private final UserService userService;

    private final ModelMapper modelMapper;

    public ManagedListServiceV1(ManagedListRepository managedListRepository, UserService userService,
                                ModelMapper modelMapper) {
        this.managedListRepository = managedListRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public ManagedListDTO createList(ManagedListDTO listDTO) {
        validateList(listDTO);
        validateOwner(listDTO.getOwnerId());

        ManagedList list = convert(listDTO);
        if (list.getItems() != null) {
            list.clearItems();
        }

        return convert(managedListRepository.save(list));
    }

    @Override
    public ManagedListDTO getManagedList(UUID id) {
        Optional<ManagedList> queryResult = managedListRepository.findById(id);
        if (queryResult.isEmpty()) {
            throw new ListNotFoundException(
                    String.format("Could not find list with specified ID %s", id));
        }

        return convert(queryResult.get());
    }

    @Override
    public ManagedListDTO updateList(UUID id, ManagedListDTO listDTO) {
        validateList(listDTO);
        ManagedList list = convert(listDTO);

        Optional<ManagedList> existing = managedListRepository.findById(id);
        if (existing.isEmpty()) {
            throw new ListNotFoundException("No list with that ID");
        }

        ManagedList existingList = existing.get();

        // TODO all text elements should be saved lowercase as labels
        // Add new elements to list
        for (ListItem item : list.getItems()) {
            if (!existingList.getItems().contains(item)) {
                existingList.addItem(item);
            }
        }

        // Remove items that were deleted
        for (ListItem item : existingList.getItems()) {
            if (!list.getItems().contains(item)) {
                existingList.removeItem(item);
            }
        }

        return convert(managedListRepository.save(existingList));
    }

    @Override
    public void deleteList(UUID id) {
        managedListRepository.deleteById(id);
    }

    private void validateOwner(UUID id) {
        try {
            userService.getUser(id);
        } catch (UserNotFoundException exception) {
            throw new ValidationException("Cannot create a list with an invalid user.");
        }
    }

    private void validateList(ManagedListDTO list) {
        if (list == null) {
            throw new IllegalArgumentException("Bad input");
        }
    }

    private ManagedListDTO convert(ManagedList list) {
        return modelMapper.map(list, ManagedListDTO.class);
    }

    private ManagedList convert(ManagedListDTO listDto) {
        return modelMapper.map(listDto, ManagedList.class);
    }
}
