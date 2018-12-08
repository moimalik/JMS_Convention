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
import javax.jms.Session;
import javax.jms.Topic;
import messages.EtatFormulaire;
import messages.Formulaire;
import messages.FormulaireEnValidation;

/**
 *
 * @author malik
 */
public class SScolariteListener implements MessageListener{

    private final MessageProducer mp;
    private final Session session;

    public SScolariteListener(Session session, MessageProducer mp) {
        this.session = session;
        this.mp = mp;
    }
    
    @Override
    public void onMessage(Message message) {
        try {
        
            Topic source = (Topic) message.getJMSDestination();

            // System.out.println("MSG RECU " + source.getTopicName());
            String topicName = source.getTopicName();

            if (topicName.equalsIgnoreCase(Nommage.TOPIC_FICHE_CONVENTION)) {

                if (message instanceof ObjectMessage) {
                    ObjectMessage om = (ObjectMessage) message;
                    Object obj = om.getObject();
                    if (obj instanceof Formulaire) {
                        Formulaire form = (Formulaire) obj;
                        System.out.println("Formulaire n° " + form.getIdConv()+ " reçue ");
                        
                        FormulaireEnValidation verif = new FormulaireEnValidation(form);
                        
                        //////////////PARTIE METIER///////////////
                        boolean vScolarite = metier(verif.getNomEtu(),verif.getPreEtu(),verif.getNumEtu(),verif.getNivEtu(),verif.getDip());
                        
                        if(vScolarite){
                            verif.setVerifScolarite(EtatFormulaire.VALIDEE);
                        }else{
                            verif.setVerifScolarite(EtatFormulaire.REFUSEE);
                        }
                        
                        // envoi de la réponse de la banque
                        ObjectMessage msg = session.createObjectMessage(verif);
                        msg.setJMSType(Nommage.MSG_VALIDATION_SCO);
                        mp.send(msg);
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(SJuridiqueListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    public boolean metier(String nom,String prenom, int num, String niveau,String inti ){
        // aucune api pour verifier les assurances
        System.out.println("Nom "+nom+" "+prenom+" N° "+String.valueOf(num) +" Niveau "+niveau+" intitulé "+inti);
        double val = Math.random();
        if (val < 0.9) {
            System.out.println("Validée");
            return true;
        } else {
            System.out.println("Non validée");
            return false;
        }
            
    }
}
