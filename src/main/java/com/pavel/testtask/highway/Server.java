/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PM
 */
public class Server {

    private ServerSocket serverSocket;
    private Properties serverProp = new Properties();

    Server() {
        try (FileInputStream fis = new FileInputStream("src/main/resources/server.properties")) {
            serverProp.load(fis);
            serverSocket = new ServerSocket(Integer.parseInt(serverProp.getProperty("server.socket.port")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startServer() throws IOException {

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new Session(socket)).start();
        }
    }
}
