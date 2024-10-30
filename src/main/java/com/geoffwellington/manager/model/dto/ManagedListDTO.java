package com.geoffwellington.manager.model.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class ManagedListDTO {
    private UUID id;

    private UUID ownerId;

    private String name;

    private Instant created;

    private Instant modified;

    private Set<ListItemDTO> items;

    public ManagedListDTO(UUID id, UUID ownerId, String name, Instant created, Instant modified,
                          Set<ListItemDTO> items) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.created = created;
        this.modified = modified;
        this.items = items;
    }

    public ManagedListDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Set<ListItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<ListItemDTO> items) {
        this.items = items;
    }
}
