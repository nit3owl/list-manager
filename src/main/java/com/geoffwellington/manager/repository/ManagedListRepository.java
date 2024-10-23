package com.geoffwellington.manager.repository;

import com.geoffwellington.manager.model.ManagedList;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagedListRepository extends JpaRepository<ManagedList, UUID> {

    ManagedList save(ManagedList list);

    Optional<ManagedList> findById(UUID id);

    void deleteById(UUID id);
}
