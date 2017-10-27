/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import Entity.Driver;
import Entity.EnterPoint;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
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
    private final Set<Driver> driverSet;
    private final Set<EnterPoint> enterPointSet;

    Session(Socket socket, Set<Driver> driverSet, Set<EnterPoint> enterPointSet) {
        this.socket = socket;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            freeResourses();
        }
        this.driverSet = driverSet;
        this.enterPointSet = enterPointSet;
    }

    private void handleEvent(Event event) {
        if (event.isEntered() == true) {
            this.event.add(event);

        } else {
            for (Iterator<Event> iterator = this.event.iterator();
                    iterator.hasNext();) {

                Event nextEvent = iterator.next();
                if (event.getDriverId() == nextEvent.getDriverId()) {

                    sendInvoice(nextEvent.getPoinName(), nextEvent.getDate(),
                            event.getPoinName(), event.getDate(), event.getDriverId());
                    iterator.remove();
                    break;
                }
            }
        }
    }

    private void sendInvoice(final String enterPoinName, final Date enterDate,
            final String exitPoinName, final Date exitDate, final int driverId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                EmailSender emailSender = new EmailSender();
                emailSender.sendMessage(emailSender.prepareMessage(getDriverEmail(driverId),
                        enterPoinName, exitPoinName, getDistance(enterPoinName, exitPoinName), enterDate, exitDate));
            }
        }).start();

    }

    private float getDistance(final String enterPoinName, final String exitPoinName) {

        float enterDistance = 0;
        float exitDistance = 0;
        for (EnterPoint ePoint : enterPointSet) {
            if (ePoint.getName().equals(enterPoinName)) {
                enterDistance = ePoint.getDistance();
            }

            if (ePoint.getName().equals(exitPoinName)) {
                exitDistance = ePoint.getDistance();
            }
        }

        return Math.abs(exitDistance - enterDistance);
    }

    private String getDriverEmail(int driverId) {
        String driversEmail = null;
        for (Driver driver : driverSet) {
            if (driver.getId() == driverId) {
                driversEmail = driver.getEmail();
                break;
            }
        }
        return driversEmail;
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

                    handleEvent((Event) msg);
                    System.out.println("from Session: " + "Point " + ((Event) msg).getPoinName()
                            + " Id водителя " + ((Event) msg).getDriverId() + " событие "
                            + ((Event) msg).isEntered());
                } else {
                    freeResourses();
                    break;
                }

            } catch (IOException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                freeResourses();
                return;
            } catch (ClassNotFoundException ex) {
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
