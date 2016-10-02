/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import com.pavel.testtask.highway.Server;
import com.pavel.testtask.highway.Client;
import com.pavel.testtask.highway.Message;
import java.io.IOException;
import java.util.Date;
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void generalTest() throws IOException, ClassNotFoundException {

        new Thread(new Runnable() {

            @Override
            public void run() {
                Server server = new Server();
                server.launchServer();
            }
        }).start();

        Client client = new Client();
        try {
            client.connectToServer();
        } catch (IOException ex) {
            System.out.println("Нет соединения с сервером");
            return;
        }

        for (int i = 0; i < 10; i++) {
            System.out.println((String) client.readFromServer());
            client.reset();
            Message msg = new Message(String.valueOf(i), i, true, new Date());
            client.sendMessage(msg);

            System.out.println("Отправлено следуещее сообщение " + "Point "
                    + msg.getPoinName() + " Id водителя " + msg.getDriverId()
                    + " событие " + msg.isEntered() + " дата " + msg.getDate());

            System.out.println((String) client.readFromServer());
            client.reset();
        }

        for (int i = 0; i < 5; i++) {
            System.out.println((String) client.readFromServer());
            client.reset();
            Message msg = new Message(String.valueOf(i + 3), i, false, new Date());
            client.sendMessage(msg);

            System.out.println("Отправлено следуещее сообщение " + "Point "
                    + msg.getPoinName() + " Id водителя " + msg.getDriverId()
                    + " событие " + msg.isEntered() + " дата " + msg.getDate());

            System.out.println((String) client.readFromServer());
            client.reset();
        }
    }
}
