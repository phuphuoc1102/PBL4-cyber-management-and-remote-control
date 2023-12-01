/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import com.entity.enums.ComputerStatus;

import java.util.UUID;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author boixi
 */
public class Computer {
    private UUID id;
    private String name;
    private double price;
    InetAddress inetAddress;
    private ComputerStatus status;

    public Computer() {
    }
    public Computer(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public UUID getId() {
        return this.id;
    }
        public InetAddress getInetAddress(){
            return this.inetAddress;
        }
        public void setInetAddress(InetAddress inet){
            this.inetAddress = inet;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public ComputerStatus getStatus() {
            return status;
        }

        public void setStatus(ComputerStatus status) {
            this.status = status;
        }
    }
