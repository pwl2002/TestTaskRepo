/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class Client {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Properties clientProp = new Properties();

    public Client() {
        try (FileInputStream fis = new FileInputStream("src/main/resources/client.properties")) {
            clientProp.load(fis);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connectToServer() throws IOException {
        socket = new Socket(clientProp.getProperty("server.host"), Integer.parseInt(clientProp.getProperty("server.socket.port")));
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

    }

    public void send(Event event) throws IOException {
        out.writeObject(event);
    }

    public Object receive() throws IOException, ClassNotFoundException {
        Object obj = in.readObject();
        out.reset();
        return obj;
    }

    public void exit() {
        try {
            out.writeObject("exit");
            freeResourses();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void freeResourses() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ex1) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException ex1) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
