/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;

/**
 *
 * @author PM
 */
public class Driver implements Serializable{
    private int id;
    private String email;

    @Override
    public boolean equals(Object obj) {
        return ((Driver)obj).id==this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public Driver(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
