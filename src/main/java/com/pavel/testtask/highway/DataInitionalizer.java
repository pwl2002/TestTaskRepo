/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import entity.Driver;
import entity.Gate;
import java.util.Set;

/**
 *
 * @author PM
 */
public interface DataInitionalizer {

    public Set<Gate> getEnterPoints();

    public Set<Driver> getRegistredDrivers();
    
}
