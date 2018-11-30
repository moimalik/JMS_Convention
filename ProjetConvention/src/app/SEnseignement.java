/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.logging.Logger;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.naming.NamingException;

/**
 *
 * @author malik
 */
public class SEnseignement extends ClientJMS{
    
    private MessageConsumer mc;
    private MessageProducer mp;
    private MessageProducer mt;
    
    
    private void setProducerConsumer() {

        try {
             // recuperation des destinations
            Destination Emis = (Destination) namingContext.lookup(Nommage.QUEUE_VALIDATION);
            
            Destination RecepTopic = (Destination) namingContext.lookup(Nommage.TOPIC_FICHE_CONVENTION);
            Destination RecepQueue = (Destination) namingContext.lookup(Nommage.QUEUE_CONFIRMATION);
            
            System.out.println("Destination lookup done.");

            // creation du consommateur et du producteur
            mc = session.createConsumer(Emis);
            
            mp = session.createProducer(RecepTopic);
            mt = session.createProducer(RecepQueue);
            
            
        } catch (JMSException | NamingException ex) {
            Logger.getLogger(ex.getMessage());
        }
    }
    
}