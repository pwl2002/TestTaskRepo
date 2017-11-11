/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;

/**
 *
 * @author PM
 */
public class Gate implements Serializable{

    private String name;
    private float distance;
    private static final long serialVersionUID = 20100515;

    
    public Gate(String name) {
        this.name = name;        
    }

    public Gate(String name, float distance) {
        this.name = name;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
