/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;
import messages.EtatFormulaire;
import messages.*;


/**
 *  service enseignement partie metier
 * 
 * @author Fouad El Ouaryaghli, Malik Belfodil
 */
public class SEnseignementListener implements MessageListener{

    private final MessageProducer mp;
    private final Session session;

    public SEnseignementListener (Session session, MessageProducer mp) {
        this.session = session;
        this.mp = mp;
        
    }
    /**
         * reception, realisation metier et expedition
         * 
         * @param message
         *  
         * @return void. 
    */
    @Override
    public void onMessage(Message message) {
        
        try {
            System.out.println("------------------------");
            
            String nameSource;
            if (message.getJMSType().equalsIgnoreCase(Nommage.MSG_DIFFUSION_AU_SERVICE)){
                Topic source = (Topic) message.getJMSDestination();
                nameSource = source.getTopicName();
            }
            else {
                Queue source = (Queue) message.getJMSDestination();
                nameSource = source.getQueueName();
            }
            

            if (nameSource.equalsIgnoreCase(Nommage.TOPIC_FICHE_CONVENTION)) {

                if (message instanceof ObjectMessage) {
                    ObjectMessage om = (ObjectMessage) message;
                    Object obj = om.getObject();
                    if (obj instanceof FormulaireEnValidation) {
                        FormulaireEnValidation form = (FormulaireEnValidation) obj;
                        System.out.println("Formulaire n° " + form.getIdConv()+ " reçue --> vérifier config Enseignement");
                        
                        //////////////PARTIE METIER///////////////
                        traitementPreConv(form);
                        // envoi de la réponse de la banque
                        ObjectMessage msg = session.createObjectMessage(form);
                        msg.setJMSType(Nommage.MSG_VALIDATION_ENS);
                        mp.send(msg);
                        
                     }
                }
            }
            
            if (nameSource.equalsIgnoreCase(Nommage.QUEUE_CONFIRMATION )){
                if (message instanceof ObjectMessage) {
                    ObjectMessage om = (ObjectMessage) message;
                    Object obj = om.getObject();
                    if (obj instanceof ValidOk) {
                        ValidOk form = (ValidOk) obj;
                        System.out.println("Formulaire n° " + form.getIdConv()+ " reçue --> vérifier config Enseignement");

                        //////////////PARTIE METIER///////////////
                        traitementValid(form);
                    }
                }
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(SJuridiqueListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
         * mise en forme du message de retour
         * 
         * @param FormulaireEnValidation formulaire 
         *  
         * @return void. 
    */
    public void traitementPreConv(FormulaireEnValidation f){
        
        double val = Math.random();
        if (val < 0.9) {
            System.out.println("Validée");
            f.setVerifEnseignement(EtatFormulaire.VALIDEE);
        } else {
            System.out.println("Non validée");
            f.setVerifEnseignement(EtatFormulaire.REFUSEE);
        }
    }
    /**
         * mise en forme du message de retour
         * 
         * @param siren siren de l'entreprise
         * @param nom nom de l'entreprise 
         * @return boolean. 
    */
    public void traitementValid(ValidOk f){
        System.out.println("Demande de Pre convention Validée !!!");
        System.out.println(f.toString());
    }
    
}
