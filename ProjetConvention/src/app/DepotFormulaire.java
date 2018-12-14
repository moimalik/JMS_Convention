/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import messages.Formulaire;

/**
 * Service dépot de formulaire
 * @author Fouad El Ouaryaghli, Malik Belfodil
 */
public class DepotFormulaire extends ClientJMS{
        private MessageProducer mp;


    /**
         * parametrage des producteurs et des consommateurs
         *  
         * @return void. 
    */
    void setProducerConsumer() {

        try {
            // recuperation de la destination
            Destination dest = (Destination) namingContext.lookup(Nommage.QUEUE_DEPOT);
            System.out.println("Destination lookup done.");

            // creation du producteur
            mp = session.createProducer(dest);

        } catch (Exception ex) {
            Logger.getLogger(DepotFormulaire.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
         * Envoie de message dans une file pour tester nos services
         *  
         * @return void. 
    */
    void runSimu() {

        // creation des formulaires
        
        //Création OK
        Formulaire f1 = new Formulaire(1, "toto", "toto", 10, "M2", "MIAGE", "Assu1", 1, "PEUGEOT SA", 552100554,
                            new GregorianCalendar(2018,2,20), new GregorianCalendar(2018,8,20), 600, "blabla");
        
        //Erreur nom entreprise
        Formulaire f2 = new Formulaire(2, "tata", "tata", 20, "M2", "MIAGE", "Assu3", 1, "PIGEOT", 552100554, 
                            new GregorianCalendar(2018,1,20), new GregorianCalendar(2018,8,20), 600, "blabla");
        
        //Erreur Numéro Siret
        Formulaire f3 = new Formulaire(3, "tata", "tata", 30, "M2", "MIAGE", "Assu3", 1, "PIGEOT", 999999999, 
                            new GregorianCalendar(2018,1,20), new GregorianCalendar(2018,8,20), 600, "blabla");
        
        //Erreur date
        Formulaire f4 = new Formulaire(4, "titi", "titi", 40, "M2", "MIAGE", "Assu2", 1, "PEUGEOT SA", 552100554,
                            new GregorianCalendar(2018,3,20), new GregorianCalendar(2018,8,20), 600,"blabla");
        
        //Rémunérarion inférieure à 500
        Formulaire f5 = new Formulaire(5, "tutu", "tutu", 50, "M2", "MIAGE", "Assu5", 1, "PEUGEOT SA", 552100554,
                            new GregorianCalendar(2018,2,20), new GregorianCalendar(2018,8,20), 400, "blabla");


       
            try {
                // creation des ObjectMessage
                ObjectMessage msg1 = session.createObjectMessage(f1);
                msg1.setJMSType(Nommage.MSG_DEPOT);
                ObjectMessage msg2 = session.createObjectMessage(f2);
                msg2.setJMSType(Nommage.MSG_DEPOT);
                ObjectMessage msg3 = session.createObjectMessage(f3);
                msg3.setJMSType(Nommage.MSG_DEPOT);
                ObjectMessage msg4 = session.createObjectMessage(f4);
                msg4.setJMSType(Nommage.MSG_DEPOT);
                ObjectMessage msg5 = session.createObjectMessage(f5);
                msg5.setJMSType(Nommage.MSG_DEPOT);
                
//                mp.send(msg1);
//                System.out.println("Form 1 envoyé");
//                mp.send(msg2);
//                System.out.println("Form 2 envoyé");
                mp.send(msg3);
                System.out.println("Form 3 envoyé");
//                mp.send(msg4);
//                System.out.println("Form 4 envoyé");
//                mp.send(msg5);
//                System.out.println("Form 5 envoyé");

            } catch (JMSException ex) {
                Logger.getLogger(DepotFormulaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    /**
     * @param args the command line arguments
     * @throws javax.jms.JMSException
     * @throws javax.naming.NamingException
     */
    public static void main(String[] args) throws JMSException, NamingException {

        DepotFormulaire dp = new DepotFormulaire();
        dp.initJMS();
        dp.setProducerConsumer();
        dp.startJMS();
        dp.runSimu();
        dp.closeJMS();
    }
}