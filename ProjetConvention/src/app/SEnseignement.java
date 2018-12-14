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
 * @author Fouad El Ouaryaghli, Malik Belfodil
 */
public class SEnseignement extends ClientJMS{
    
    private MessageConsumer mc1;
    private MessageConsumer mc2;
    private MessageProducer mp;
    
    
    void setProducerConsumer() {

        try {
             // recuperation des destinations
            Destination Emis = (Destination) namingContext.lookup(Nommage.QUEUE_VALIDATION);
            
            Destination RecepTopic = (Destination) namingContext.lookup(Nommage.TOPIC_FICHE_CONVENTION);
            Destination RecepQueue = (Destination) namingContext.lookup(Nommage.QUEUE_CONFIRMATION);
            
            System.out.println("Destination lookup done.");

            // creation du consommateur et du producteur
            mp = session.createProducer(Emis);
            
            mc1 = session.createConsumer(RecepTopic);
            mc2 = session.createConsumer(RecepQueue);
            
            mc1.setMessageListener(new SEnseignementListener(session, mp));
            mc2.setMessageListener(new SEnseignementListener(session, mp));
            
            
        } catch (JMSException | NamingException ex) {
            Logger.getLogger(ex.getMessage());
        }
    }
    public static void main(String[] args) throws Exception {

        SEnseignement serviceEnseignement = new SEnseignement();
        serviceEnseignement.initJMS();
        serviceEnseignement.setProducerConsumer();
        serviceEnseignement.startJMS();
        System.out.println("*** Service du département d'enseignement démarré. ***");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("Appuyez sur 'Q' pour quitter.");
        } while (!br.readLine().equalsIgnoreCase("Q"));
        serviceEnseignement.closeJMS();
    }
}
