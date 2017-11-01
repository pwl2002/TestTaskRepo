/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import Entity.Driver;
import Entity.Gate;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author PM
 */
public class HighwayTest {

    public HighwayTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private void startConversation(Client client) throws IOException, ClassNotFoundException {
        for (int i = 0; i < 10; i++) {
            System.out.println((String) client.receive());
            Event event = new Event(new Gate(String.valueOf(i)), new Driver(i, "pwl2002@ukr.net"), true, new Date());
            client.send(event);

            System.out.println("Отправлено следуещее сообщение " + "Point "
                    + event.getGate().getName() + " Id водителя " + event.getDriver()
                    + " событие " + event.isIsEntered() + " дата " + event.getDate());

            System.out.println((String) client.receive());
        }

        for (int i = 0; i < 1; i++) {
            System.out.println((String) client.receive());
            Event event = new Event(new Gate(String.valueOf(i + 3)), new Driver(i, "pwl2002@ukr.net"), false, new Date());
            client.send(event);

            System.out.println("Отправлено следуещее сообщение " + "Point "
                    + event.getGate().getName() + " Id водителя " + event.getDriver()
                    + " событие " + event.isIsEntered() + " дата " + event.getDate());

        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HighwayTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        client.exit();

    }

    @Test
    public void startTest() throws IOException, ClassNotFoundException, InterruptedException {

        startServer();

        for (int i = 0; i < 2; i++) {
            startClient();
        }

        Thread.currentThread().join(5000);
    }

    public void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Server server = new Server();
                try {
                    server.startServer();
                } catch (IOException ex) {
                    Logger.getLogger(HighwayTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public void startClient() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Client client = new Client();

                try {
                    client.connectToServer();
                    startConversation(client);

                } catch (IOException ex) {
                    Logger.getLogger(HighwayTest.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(HighwayTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

    }
}
