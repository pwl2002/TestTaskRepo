/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PM
 */
public class Session implements Runnable {

    private Socket socket;
    private Server server;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    @Override
    public void run() {
        boolean nonExit = true;
        while (nonExit) {
            try {

                out.writeObject("S:::Готов читать ");
                out.reset();
                //System.out.println("готов читать");
                Message msg = (Message) in.readObject();
                out.writeObject("S:::Успешно прочитано ");
                out.reset();
                
                server.processing(msg);
                System.out.println("Point " + msg.getPoinName() + " Id водителя " + msg.getDriverId() + " событие " + msg.isEntered());
//                out.reset();
                          

            } catch (IOException ex) {
                //System.out.println("No Connection");
                nonExit = false;
                freeResourses();
            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
//                freeResourses();
//                nonExit = false;
            }
        }
    }

    private void freeResourses() {
        System.out.println("Сессия прервана");
        if (in != null) {
            try {
                in.close();
                System.out.println("In закрыт");
            } catch (IOException ex1) {
//                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        if (out != null) {
            try {
                out.close();
                System.out.println("Out закрыт");
            } catch (IOException ex1) {
//                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        try {
            socket.close();
            System.out.println("Сокет закрыт");
        } catch (IOException ex1) {
//            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    public Session(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            freeResourses();
        }
        System.out.println("создал сессию");
    }

}
