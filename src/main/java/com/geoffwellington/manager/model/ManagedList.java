package com.geoffwellington.manager.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    // One list can have many ListItems
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn
    private Set<ListItem> items = new LinkedHashSet<>();

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

    public Set<ListItem> getItems() {
        return items;
    }

    public void setItems(Set<ListItem> items) {
        this.items = items;
    }

    public void clearItems() {
        this.items.clear();
    }

    public void addItem(ListItem item) {
        items.add(item);
        item.setList(this);
    }

    public void removeItem(ListItem item) {
        items.remove(item);
        item.setList(null);
    }
}
