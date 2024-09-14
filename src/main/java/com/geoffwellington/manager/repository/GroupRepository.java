package com.geoffwellington.manager.repository;

import com.geoffwellington.manager.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<Membership, UUID> {
}
