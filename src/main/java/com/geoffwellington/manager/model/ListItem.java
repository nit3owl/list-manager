package com.geoffwellington.manager.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "items")
public class ListItem {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    private ManagedList list;

    @Column
    private String text;

    @Column
    private String label;

    @Column
    private int quantity;

    public ListItem(long id, ManagedList list, String text, String label, int quantity) {
        this.id = id;
        this.list = list;
        this.text = text;
        this.label = label;
        this.quantity = quantity;
    }

    public ListItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListItem listItem = (ListItem) o;
        return id == listItem.id && quantity == listItem.quantity && Objects.equals(list, listItem.list) && Objects.equals(text, listItem.text) && Objects.equals(label, listItem.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, list, text, label, quantity);
    }
}
