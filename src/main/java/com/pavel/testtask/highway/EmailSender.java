/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author PM
 */
class EmailSender {

    private final Properties postServiceProperties;

    public EmailSender() {

        this.postServiceProperties = new Properties();
        try {
            this.postServiceProperties.load(new FileInputStream("src/main/resources/post_service_properties.properties"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public javax.mail.Message prepareMessage(String emailTo, String enterPoint, String exitPoint, float distance, Date enterDate, Date exitDate) {
        javax.mail.Message message = null;
        try {
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(postServiceProperties,
                    new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(postServiceProperties.getProperty("username"),
                            postServiceProperties.getProperty("password"));
                }
            });

            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(postServiceProperties.getProperty("from")));
            message.setRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(/*emailTo*/"pwl2002@ukr.net"));
            message.setSubject("Счет за проезд по автобану" + this.toString());
            message.setText("Пункт въезда " + enterPoint + " Время въезда "
                    + enterDate + " Пункт выезда "
                    + exitPoint + " Время выезда " + exitDate + " Дистанция "
                    + distance + " Стоимость " + distance * Float.parseFloat(postServiceProperties.getProperty("price.for.km")));

        } catch (MessagingException ex) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
        }

        return message;
    }

    public void sendMessage(javax.mail.Message message) {
        try {
            Transport.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
