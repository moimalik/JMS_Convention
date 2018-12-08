/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.naming.NamingException;

/**
 *
 * @author Fouad El Ouaryaghli
 */
public class SScolarite extends ClientJMS{
    
    private MessageConsumer mc;
    private MessageProducer mp;
    
    void setProducerConsumer() {

        try {
             // recuperation des destinations
            Destination Emis = (Destination) namingContext.lookup(Nommage.QUEUE_VALIDATION);
            Destination Recep = (Destination) namingContext.lookup(Nommage.TOPIC_FICHE_CONVENTION);
            System.out.println("Destination lookup done.");

            // creation du consommateur et du producteur
            mc = session.createConsumer(Recep);
            mp = session.createProducer(Emis);
            
            mc.setMessageListener(new SScolariteListener(session, mp));
            
        } catch (JMSException | NamingException ex) {
            Logger.getLogger(ex.getMessage());
        }
    }
   
    public static void main(String[] args) throws Exception {

        SScolarite serviceScolarite = new SScolarite();
        serviceScolarite.initJMS();
        serviceScolarite.setProducerConsumer();
        serviceScolarite.startJMS();
        System.out.println("*** Service de scolarité a démarré. ***");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("Appuyez sur 'Q' pour quitter.");
        } while (!br.readLine().equalsIgnoreCase("Q"));
        serviceScolarite.closeJMS();
    }
}
