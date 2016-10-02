/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PM
 */
public class Client implements Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Properties prop;

    public void connectToServer() throws IOException {
        prop = new Properties();
        prop.load(new FileInputStream("src/main/resources/config.properties"));
        
        //socket = new Socket("localhost", 7654);
        socket = new Socket(prop.getProperty("server.host"), Integer.parseInt(prop.getProperty("server.socket.port")));
        
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }


    public void sendMessage(Message msg) throws IOException {
        out.writeObject(msg);
    }

    @Override
    public void run() {
        try {
            connectToServer();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Object readFromServer() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    public void reset() {
        try {
            out.reset();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  

    public void freeResourses() {
        System.out.println("Соединение с сервером прервано");
        if (in != null) {
            try {
                in.close();
                System.out.println("In закрыт");
            } catch (IOException ex1) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        if (out != null) {
            try {
                out.close();
                System.out.println("Out закрыт");
            } catch (IOException ex1) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        try {
            socket.close();
            System.out.println("Сокет закрыт");
        } catch (IOException ex1) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

}
