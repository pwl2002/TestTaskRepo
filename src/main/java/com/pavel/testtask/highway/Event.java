/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import entity.Driver;
import entity.Gate;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author PM
 */
public class Event implements Serializable {

    private Gate gate;
    private Driver driver;
    private Date date;
    private boolean isEntered;
    private static final long serialVersionUID = 20100515;


    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driverId) {
        this.driver = driverId;
    }

    public Date getDate() {
        System.out.println(this.date.toString());
        return new Date(this.date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

    Event(Gate gate, Driver driver, boolean event, Date date) {
        this.gate = gate;
        this.driver = driver;
        this.date = date;
        this.isEntered = event;
    }

    public boolean isIsEntered() {
        return isEntered;
    }

    public void setIsEntered(boolean isEntered) {
        this.isEntered = isEntered;
    }
}
