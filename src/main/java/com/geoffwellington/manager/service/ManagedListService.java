package com.geoffwellington.manager.service;

import com.geoffwellington.manager.model.ManagedList;

import java.util.UUID;

public interface ManagedListService {

    ManagedList getManagedList(UUID uuid);
}
