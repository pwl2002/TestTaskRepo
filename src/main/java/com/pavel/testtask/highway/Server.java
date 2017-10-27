/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import Entity.Driver;
import Entity.EnterPoint;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
public class Server {

    private ServerSocket serverSocket;
    private final Set<Driver> driverSet = new HashSet<>();
    private final Set<EnterPoint> enterPointsSet = new HashSet<>();
    private Properties serverProp = new Properties();

    Server() {
        try {
            serverProp.load(new FileInputStream("src/main/resources/server.properties"));
            serverSocket = new ServerSocket(Integer.parseInt(serverProp.getProperty("server.socket.port")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startServer() throws IOException {

//        initDataFromDB();
        while (true) {
            Socket socket = serverSocket.accept();

            new Thread(new Session(socket, driverSet, enterPointsSet)).start();

        }
    }

    private void initDataFromDB() {
        try {

            MongoClient mongo = new MongoClient(/*"localhost", 27017*/
                    serverProp.getProperty("db.host"), Integer.parseInt(serverProp.getProperty("db.port")));
            DB db = mongo.getDB(serverProp.getProperty("db.name"));

            DBCollection table = db.getCollection("Driver");
            DBCursor cur = table.find();
            List<DBObject> driverList = cur.toArray();

            for (DBObject drv : driverList) {
                Driver d = new Driver();
                d.setId((int) drv.get("id"));
                d.setEmail((String) drv.get("email"));
                driverSet.add(d);
            }

            table = db.getCollection("EnterPoint");
            cur = table.find();
            List<DBObject> pointList = cur.toArray();

            for (DBObject drv : pointList) {
                EnterPoint d = new EnterPoint();
                d.setDistance((float) ((int) drv.get("distance")));
                d.setName((String) drv.get("name"));
                enterPointsSet.add(d);
            }

        } catch (UnknownHostException | MongoException e) {
        }
    }

}
