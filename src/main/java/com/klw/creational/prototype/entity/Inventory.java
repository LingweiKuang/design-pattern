package com.klw.creational.prototype.entity;

import lombok.Data;

@Data
public class Inventory {
    private int stock;
    private String warehouse;

    public Inventory(int stock, String warehouse) {
        this.stock = stock;
        this.warehouse = warehouse;
    }

    public Inventory() {
    }

    public Inventory copy() {
        Inventory inv = new Inventory();
        inv.stock = this.stock;
        inv.warehouse = this.warehouse;
        return inv;
    }
}
