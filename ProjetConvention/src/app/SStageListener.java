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
import messages.Formulaire;

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
                        System.out.println("Formulaire " + form.getNumCommande() + " reçue --> vérifier coord. bancaires");
                        String iban = form.getClient().getIban();
                        // System.out.println("IBAN : " + iban);
                        // TODO: Client SOAP ou REST pour effectuer la vérif
                        
                        
              //////////////PARTIE METIER///////////////
                        
                        /*
                        // Ici, simu par tirage aléatoire
                        double r = Math.random();
                        if (r > 0.3) {
                            // Vérif OK (~ 70%)
                            cmd.setBanqueValide(true);
                            System.out.println("\t --> Coord. bancaires OK");
                        } else {
                            // Vérif KO (~ 30%)
                            cmd.setBanqueValide(false);
                            System.out.println("\t --> Coord. bancaires NOK");
                        }*/
                        // envoi de la réponse de la banque
                        ObjectMessage msg = session.createObjectMessage(cmd);
                        msg.setJMSType(Nommage.MSG_FACTURATION);
                        mp.send(msg);
                    }
                }
            }

            if (topicName.equalsIgnoreCase(Nommage.TOPIC_CMDS_TRAITEES)) {

                if (message instanceof ObjectMessage) {
                    ObjectMessage om = (ObjectMessage) message;
                    Object obj = om.getObject();
                    if (obj instanceof Commande) {
                        Commande cmd = (Commande) obj;
                        System.out.println("Commande " + cmd.getNumCommande() + " traitée reçue --> effectuer débit");
                        System.out.println("\t TODO...");

                        // TODO: Client SOAP ou REST pour effectuer le débit
                    }
                }

            }
        } catch (JMSException ex) {
            Logger.getLogger(FacturationListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
