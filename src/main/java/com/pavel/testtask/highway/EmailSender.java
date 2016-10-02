/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.highway;

import java.util.Date;
import java.util.Properties;
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
class EmailSender implements Runnable {
    
    private final String emailTo;
    private final float distance;
    private final String enterPoinName;
    private final String exitPoinName;
    private Date enterDate;
    private Date exitDate;
    private Properties prop;
    
    public EmailSender(Properties prop, String emailTo, float distance, String enterPoinName,
            Date enterDate, String exitPoinName, Date exitDate) {
        this.emailTo = emailTo;
        this.distance = distance;
        this.enterPoinName = enterPoinName;
        this.exitPoinName = exitPoinName;
        this.enterDate = enterDate;
        this.exitDate = exitDate;
        this.prop = prop;
    }
    
    @Override
    public void run() {
        
        String from = prop.getProperty("from");
        String host = prop.getProperty("host");
        final String password = prop.getProperty("password");
        final String username = prop.getProperty("username");
        String port = prop.getProperty("port");
        
        float price = distance * Float.parseFloat(prop.getProperty("price.for.km"));
        
        Properties props = System.getProperties();
        
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        
        try {
            javax.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(emailTo));
            message.setSubject("Счет за проезд по автобану");
            message.setText("Пункт въезда " + enterPoinName + " Время въезда "
                    + enterDate + " Пункт выезда "
                    + exitPoinName + " Время выезда " + exitDate + " Дистанция "
                    + distance + " Стоимость " + price);            
            
            Transport.send(message);
            System.out.println("Sent message successfully....");
            
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
}
