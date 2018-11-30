/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
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
public class SStage extends ClientJMS{
  
    private MessageConsumer mc;
    private MessageProducer mp1;
    private MessageProducer mp2;
    
        private void setProducerConsumer() {

        try {
            
            // recuperation des destinations
            Destination formEmis = (Destination) namingContext.lookup(Nommage.TOPIC_FICHE_CONVENTION);
            Destination formValides = (Destination) namingContext.lookup(Nommage.QUEUE_VALIDATION);
            Destination formConfirmes = (Destination) namingContext.lookup(Nommage.QUEUE_CONFIRMATION);
            System.out.println("Destination lookup done.");

            // creation des consommateurs et du producteur
            mc = session.createConsumer(formValides);
            mp1 = session.createProducer(formEmis);
            mp2 = session.createProducer(formConfirmes);

            // Quel MessageProducer doit on choisir
            SStageListener fl = new SStageListener(session, mp1);
            mc.setMessageListener(fl);

        } catch (JMSException | NamingException ex) {
            Logger.getLogger(ex.getMessage());
        }
    }
        
        
    public static void main(String[] args) throws Exception {

        SStage serviceStage = new SStage();
        serviceStage.initJMS();
        serviceStage.setProducerConsumer();
        serviceStage.startJMS();
        System.out.println("*** Service de stage démarré. ***");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("Appuyez sur 'Q' pour quitter.");
        } while (!br.readLine().equalsIgnoreCase("Q"));
        serviceStage.closeJMS();
    }
}
