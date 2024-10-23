package com.geoffwellington.manager.model.dto;

import java.util.Objects;

public class ListItemDTO {
    private String text;

    private int quantity;

    public ListItemDTO(String text, int quantity) {
        this.text = text;
        this.quantity = quantity;
    }

    public ListItemDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        ListItemDTO itemDTO = (ListItemDTO) o;
        return quantity == itemDTO.quantity && Objects.equals(text, itemDTO.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, quantity);
    }
}
