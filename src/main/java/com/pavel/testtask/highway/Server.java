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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
    private Set<Message> trafic = new HashSet<>();
    private Set<Driver> driverSet = new HashSet<>();
    private Set<EnterPoint> enterPointSet = new HashSet<>();
    private Properties prop;

    public void launchServer() {
        
        prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            serverSocket = new ServerSocket(Integer.parseInt(prop.getProperty("server.socket.port")));
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        initDataFromDB();

        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                System.out.println("подключился к сокету");
                new Session(socket, this).run();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    synchronized public void processing(Message msg) {
        if (msg.isEntered() == true) {
            trafic.add(msg);
        } else {
            for (Iterator<Message> iterator = trafic.iterator();
                    iterator.hasNext();) {

                Message next = iterator.next();
                if (msg.getDriverId() == next.getDriverId()) {

                    sendChek(next.getPoinName(), next.getDate(),
                            msg.getPoinName(), msg.getDate(), msg.getDriverId());
                    iterator.remove();

                    break;
                }
            }
        }
    }

    private void sendChek(String enterPoinName, Date enterDate,
            String exitPoinName, Date exitDate, int driverId) {
        String emailTo = "";
        float enterDistance = 0;
        float exitDistance = 0;

        for (Driver driver : driverSet) {
            if (driver.getId() == driverId) {
                emailTo = driver.getEmail();
                break;
            }
        }
        for (EnterPoint ePoint : enterPointSet) {
            if (ePoint.getName().equals(enterPoinName)) {
                enterDistance = ePoint.getDistance();
            }

            if (ePoint.getName().equals(exitPoinName)) {
                exitDistance = ePoint.getDistance();
            }
        }

        new EmailSender(prop, emailTo, Math.abs(exitDistance - enterDistance),
                enterPoinName, enterDate, exitPoinName, exitDate).run();
    }

    private void initDataFromDB() {
        try {

            MongoClient mongo = new MongoClient(/*"localhost", 27017*/
            prop.getProperty("db.host"), Integer.parseInt(prop.getProperty("db.port")));
            DB db = mongo.getDB(/*"testdb"*/prop.getProperty("db.name"));

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
                enterPointSet.add(d);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }   

}
