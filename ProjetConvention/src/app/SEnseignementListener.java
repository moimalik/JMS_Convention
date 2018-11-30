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
import javax.jms.Session;
import javax.jms.Topic;
import messages.Formulaire;
import messages.ValidOk;


/**
 *
 * @author malik
 */
public class SEnseignementListener implements MessageListener{

    private final MessageProducer mp;
    private final Session session;

    public SEnseignementListener (Session session, MessageProducer mp) {
        this.session = session;
        this.mp = mp;
        
    }
    @Override
    public void onMessage(Message message) {
        
        
        
        try {
        
            Topic source = (Topic) message.getJMSDestination();

            // System.out.println("MSG RECU " + source.getTopicName());
            String topicName = source.getTopicName().replace('_', '/');

            if (topicName.equalsIgnoreCase(Nommage.TOPIC_FICHE_CONVENTION)) {

                if (message instanceof ObjectMessage) {
                    ObjectMessage om = (ObjectMessage) message;
                    Object obj = om.getObject();
                    if (obj instanceof Formulaire) {
                        Formulaire form = (Formulaire) obj;
                        System.out.println("Formulaire n° " + form.getIdConv()+ " reçue --> vérifier config Enseignement");
                        int val = form.getIdConv();
                        
                        //////////////PARTIE METIER///////////////
                        traitementPreConv(form);
                        
                        // envoi de la réponse de la banque
                        ObjectMessage msg = session.createObjectMessage();
                        msg.setJMSType(Nommage.MSG_VALIDATION_JUR);
                        mp.send(msg);
                    }
                }
            }else{
                if (topicName.equalsIgnoreCase(Nommage.QUEUE_CONFIRMATION )){
                    if (message instanceof ObjectMessage) {
                        ObjectMessage om = (ObjectMessage) message;
                        Object obj = om.getObject();
                        if (obj instanceof ValidOk) {
                            ValidOk form = (ValidOk) obj;
                            System.out.println("Formulaire n° " + form.getIdConv()+ " reçue --> vérifier config Enseignement");
                            int val = form.getIdConv();

                            //////////////PARTIE METIER///////////////
                            traitementValid(form);

                        }
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(SJuridiqueListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void traitementPreConv(Formulaire f){
        
        System.out.println("Demande de Pre convention");
        System.out.println(f.toString());
        System.out.println("saisir la reponse à la demande");
        
        Scanner sc = new Scanner(System.in);
            String s = sc.next();
        //faire set de s dans message
    }
    public void traitementValid(ValidOk f){
        System.out.println("Demande de Pre convention Valider !!!");
        System.out.println(f.toString());
    }
    
}
