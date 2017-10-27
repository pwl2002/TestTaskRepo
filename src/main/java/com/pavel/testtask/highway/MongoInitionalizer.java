/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import Entity.Driver;
import Entity.Gate;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PM
 */
public class MongoInitionalizer implements DataInitionalizer {

    private DB db;
    Properties dbProperties = null;

    public MongoInitionalizer() {

        try {
            dbProperties.load(new FileInputStream("src/main/resources/client.properties"));
            MongoClient mongo = new MongoClient(/*"localhost", 27017*/
                    dbProperties.getProperty("db.host"), Integer.parseInt(dbProperties.getProperty("db.port")));
            db = mongo.getDB(dbProperties.getProperty("db.name"));

        } catch (UnknownHostException ex) {
            Logger.getLogger(MongoInitionalizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MongoInitionalizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Set<Gate> getEnterPoints() {
        DBCollection table = db.getCollection("EnterPoint");
        DBCursor cur = table.find();
        List<DBObject> pointList = cur.toArray();

        Set<Gate> enterPointsSet = new HashSet<>();
        for (DBObject drv : pointList) {
            Gate d = new Gate((String) drv.get("name"), (float) ((int) drv.get("distance")));
            enterPointsSet.add(d);
        }
        return enterPointsSet;
    }

    @Override
    public Set<Driver> getRegistredDrivers() {
        DBCollection table = db.getCollection("Driver");
        DBCursor cur = table.find();
        List<DBObject> driverList = cur.toArray();

        Set<Driver> driversSet = new HashSet<>();

        for (DBObject drv : driverList) {
            driversSet.add(new Driver((int) drv.get("id"), (String) drv.get("email")));
        }
        return driversSet;
    }
}
