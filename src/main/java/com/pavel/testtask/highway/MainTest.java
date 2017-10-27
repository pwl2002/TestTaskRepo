/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PM
 */
public class MainTest {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Server().startServer();
                } catch (IOException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();

        startClient();
        startClient();
    }

    public static void startClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Client client = new Client();
                    client.connectToServer();
                    while (true) {                        
                        
                    }

                } catch (IOException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

}
