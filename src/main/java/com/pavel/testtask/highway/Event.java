/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author PM
 */
public class Event implements Serializable{
    private String poinName;
    private int driverId;
    private boolean entered;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    Event(String poinName, int driverId, boolean event, Date date) {
        this.poinName = poinName;
        this.driverId = driverId;
        this.entered = event;
        this.date = date;
    }

    public String getPoinName() {
        return poinName;
    }

    public void setPoinName(String poinName) {
        this.poinName = poinName;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public boolean isEntered() {
        return entered;
    }

    public void setEntered(boolean entered) {
        this.entered = entered;
    }
    
}
