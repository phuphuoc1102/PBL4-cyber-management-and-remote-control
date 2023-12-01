/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.entity.enums.OrderStatus;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

/**
 *
 * @author boixi
 */
public class Order {
    private UUID id;
    private Computer computer;
    private Client client;
    private Timestamp order_time;
    private Timestamp completed_time;
    private Timestamp canceled_time;
    private double total_price;
    private OrderStatus status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Timestamp getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Timestamp order_time) {
        this.order_time = order_time;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Timestamp getCompleted_time() {
        return completed_time;
    }

    public void setCompleted_time(Timestamp completed_time) {
        this.completed_time = completed_time;
    }

    public Timestamp getCanceled_time() {
        return canceled_time;
    }

    public void setCanceled_time(Timestamp canceled_time) {
        this.canceled_time = canceled_time;
    }
}
