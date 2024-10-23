package com.geoffwellington.manager.service;

import com.geoffwellington.manager.model.dto.ManagedListDTO;
import java.util.UUID;

public interface ManagedListService {

    ManagedListDTO createList(ManagedListDTO listDTO);

    ManagedListDTO getManagedList(UUID id);

    ManagedListDTO updateList(UUID id, ManagedListDTO listDTO);

    void deleteList(UUID id);
}
