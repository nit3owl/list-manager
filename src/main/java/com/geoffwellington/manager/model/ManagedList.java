package com.geoffwellington.manager.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "list")
@EntityListeners(AuditingEntityListener.class)
public class ManagedList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private User owner;

    @Column
    private String name;

    @CreatedDate
    @Column
    private Instant created;

    @LastModifiedDate
    @Column
    private Instant modified;

    @OneToMany(mappedBy = "list")
    private LinkedHashSet<ListItem> items;

    public ManagedList(UUID id, User owner, String name, Instant created, Instant modified, LinkedHashSet<ListItem> items) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.created = created;
        this.modified = modified;
        this.items = items;
    }

    public ManagedList() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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

    public LinkedHashSet<ListItem> getItems() {
        return items;
    }

    public void setItems(LinkedHashSet<ListItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagedList that = (ManagedList) o;
        return Objects.equals(id, that.id) && Objects.equals(owner, that.owner) && Objects.equals(name, that.name) && Objects.equals(created, that.created) && Objects.equals(modified, that.modified) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, name, created, modified, items);
    }
}
