package com.geoffwellington.manager.repository;

import com.geoffwellington.manager.model.ManagedList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManagedListRepository extends JpaRepository<ManagedList, UUID> {
}
