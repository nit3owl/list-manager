package com.geoffwellington.manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "item")
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Many ListItems belong to one List
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private ManagedList list;

    @Column
    private String text;

    @Column
    private String label;

    @Column
    private int quantity;

    public ListItem() {
    }

    public ListItem(UUID id, ManagedList list, String text, String label, int quantity) {
        this.id = id;
        this.list = list;
        this.text = text;
        this.label = label;
        this.quantity = quantity;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ManagedList getList() {
        return list;
    }

    public void setList(ManagedList list) {
        this.list = list;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListItem item = (ListItem) o;
        return Objects.equals(label, item.label);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(label);
    }
}


