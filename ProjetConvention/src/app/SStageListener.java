/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

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
import messages.*;

/**
 *
 * @author Fouad El Ouaryaghli
 */
public class SStageListener implements MessageListener {

    private final MessageProducer mp;
    private final Session session;

    public SStageListener(Session session, MessageProducer mp) {
        this.session = session;
        this.mp = mp;
        
    }

    @Override
    public void onMessage(Message message) {

        try {
            Queue source = (Queue) message.getJMSDestination();

            // System.out.println("MSG RECU " + source.getTopicName());
            String topicName = source.getQueueName().replace('_', '/');

            if (topicName.equalsIgnoreCase(Nommage.QUEUE_VALIDATION)) {

                if (message instanceof ObjectMessage) {
                    ObjectMessage om = (ObjectMessage) message;
                    Object obj = om.getObject();
                    if (obj instanceof Formulaire) {
                        Formulaire form = (Formulaire) obj;
                        System.out.println("Formulaire n° " + form.getIdConv()+ " reçue --> vérifier coord. bancaires");
                        boolean val = true ;//= form.getValidEns();
                        
                        
              //////////////PARTIE METIER///////////////
                        
                        
                        if (val) {
                            System.out.println("\t --> ");
                        } else {
                            System.out.println("\t --> ");
                        }
                        // envoi de la réponse de la banque
                        ObjectMessage msg = session.createObjectMessage(form);
                        msg.setJMSType(Nommage.MSG_FORM_VALIDE);
                        mp.send(msg);
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(SStageListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
