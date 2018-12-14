/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import fr.miage.toulouse.m2.eai.clientrest.metiersiren.SirenPOJO;
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
public class SJuridique extends ClientJMS{
    
    private MessageConsumer mc;
    private MessageProducer mp;
    
        void setProducerConsumer() {

        try {
            
            // recuperation des destinations
            Destination formEmis = (Destination) namingContext.lookup(Nommage.TOPIC_FICHE_CONVENTION);
            Destination formValides = (Destination) namingContext.lookup(Nommage.QUEUE_VALIDATION);
            System.out.println("Destination lookup done.");

            // creation des consommateurs et du producteur
            mc = session.createConsumer(formEmis);
            mp = session.createProducer(formValides);

            // Quel MessageProducer doit on choisir
            mc.setMessageListener(new SJuridiqueListener(session, mp));

        } catch (JMSException | NamingException ex) {
            Logger.getLogger(ex.getMessage());
        }
    }
        
        public static void main(String[] args) throws Exception {
            
        SJuridique monService = new SJuridique();
        monService.initJMS();
        monService.setProducerConsumer();
        monService.startJMS();
        System.out.println("*** Service juridique démarré. ***");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        do {
            System.out.println("Appuyez sur 'Q' pour quitter.");
        } while (!br.readLine().equalsIgnoreCase("Q"));
        monService.closeJMS();
    }
}
