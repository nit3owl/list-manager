package com.geoffwellington.manager.repository;

import com.geoffwellington.manager.model.Membership;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Membership, UUID> {
}
