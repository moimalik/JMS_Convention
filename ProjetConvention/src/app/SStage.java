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
public class SStage extends ClientJMS{
  
    private MessageConsumer mc1;
    private MessageConsumer mc2;
    private MessageProducer mp1;
    private MessageProducer mp2;
    
    void setProducerConsumer() {

        try {
            
            // recuperation des destinations
            Destination formRecu = (Destination) namingContext.lookup(Nommage.QUEUE_DEPOT);
            Destination formEmis = (Destination) namingContext.lookup(Nommage.TOPIC_FICHE_CONVENTION);
            Destination formValides = (Destination) namingContext.lookup(Nommage.QUEUE_VALIDATION);
            Destination formConfirmes = (Destination) namingContext.lookup(Nommage.QUEUE_CONFIRMATION);
            System.out.println("Destination lookup done.");

            // creation des consommateurs et du producteur
            mc1 = session.createConsumer(formRecu);
            mc2 = session.createConsumer(formValides);
            mp1 = session.createProducer(formEmis);
            mp2 = session.createProducer(formConfirmes);

//             Quel MessageProducer doit on choisir
            mc1.setMessageListener(new SStageListener(session, mp1));
            mc2.setMessageListener(new SStageListener(session, mp2));

        } catch (JMSException | NamingException ex) {
            Logger.getLogger(ex.getMessage());
        }
    }
    
        
    public static void main(String[] args) throws Exception {
        
        SStage serviceStage = new SStage();
        serviceStage.initJMS();
        
        System.out.println("1");
        serviceStage.setProducerConsumer();
        
        System.out.println("2");
        serviceStage.startJMS();
        
        System.out.println("3");
        System.out.println("*** Service de stage démarré. ***");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("Appuyez sur 'Q' pour quitter.");
        } while (!br.readLine().equalsIgnoreCase("Q"));
        serviceStage.closeJMS();
    }
}
