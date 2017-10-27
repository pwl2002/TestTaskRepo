/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import Entity.Gate;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PM
 */
public class Session implements Runnable {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Set<Event> event = new HashSet<>();


    Session(Socket socket) {
        this.socket = socket;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            freeResourses();
        }
    }

    private void handleEvent(Event event) {

        if (event.isEntered() == true) {
            this.event.add(event);

        } else {
            for (Iterator<Event> iterator = this.event.iterator();
                    iterator.hasNext();) {

                Event enteredEvent = iterator.next();
                if (event.getDriver().equals(enteredEvent.getDriver())) {
                    sendInvoice(event, enteredEvent);
                    iterator.remove();
                    break;
                }
            }
        }
    }

    private void sendInvoice(final Event exitEvent, final Event enteredEvent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EmailSender emailSender = new EmailSender();
                emailSender.sendMessage(emailSender.prepareMessage(exitEvent.getDriver().getEmail(),
                        enteredEvent.getGate().getName(), exitEvent.getGate().getName(),
                        getDistance(enteredEvent.getGate(), exitEvent.getGate()), enteredEvent.getDate(), exitEvent.getDate()));
            }
        }).start();
    }

    private float getDistance(final Gate enterPoinName, final Gate exitPoinName) {

        return Math.abs(enterPoinName.getDistance() - exitPoinName.getDistance());
    }

    @Override
    public void run() {
        startSession();
    }

    public void startSession() {
        while (true) {
            try {
                out.writeObject("S:::Готов читать ");
                out.reset();
                Object msg = in.readObject();
                if (msg instanceof Event) {
                    out.writeObject("S:::Успешно прочитано ");
                    out.reset();
                    System.out.println("Point " + ((Event) msg).getGate().getName()
                            + " Id водителя " + ((Event) msg).getDriver() + " событие "
                            + ((Event) msg).isEntered());

                    handleEvent((Event) msg);

                } else {
                    freeResourses();
                    break;
                }

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                freeResourses();
                return;
            }
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
