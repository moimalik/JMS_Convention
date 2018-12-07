/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.HashMap;
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
    
    final HashMap<Integer, FormulaireEnValidation> formEnAttente;

    public SStageListener(Session session, MessageProducer mp) {
        this.session = session;
        this.mp = mp;
        
        this.formEnAttente = new HashMap();
    }

    private boolean formulaireConfirmee(int key) {
        
        if (formEnAttente.containsKey(key)) {
            if (formEnAttente.get(key).getVerifEnseignement() == EtatFormulaire.VALIDEE &&
                    formEnAttente.get(key).getVerifJuridique()== EtatFormulaire.VALIDEE &&
                    formEnAttente.get(key).getVerifScolarite()== EtatFormulaire.VALIDEE)
            return true;
        }
        return false;
    }
    
    @Override
    public void onMessage(Message msg) {
try 
        {
            Formulaire form;
            String type;
            
            if (msg instanceof ObjectMessage) 
            {
                ObjectMessage om = (ObjectMessage) msg;
                type = om.getJMSType();
                System.out.println(type);
                Object obj = om.getObject();
                if (obj instanceof Formulaire) 
                {
                    form = (Formulaire) obj;
                    System.out.println(form.getIdConv());
                } else if (obj instanceof FormulaireEnValidation)
                {
                    form = (FormulaireEnValidation) obj;
                    return;
                } else {
                    return;
                }
            } else
            {
                return;
            }
            
            System.out.println(form.getIdConv());
            
            //reception d'un formulaire en validation depuis les différents services
            if (formEnAttente.containsKey(form.getIdConv())) 
            {
                // Formaulaire deja recue
                
                System.out.println("--> Formulaire " + form.getIdConv() + " recue");

                if (formulaireConfirmee(form.getIdConv())) 
                {
                    
                    System.out.println("--> Formulaire de pré-convention " + form.getIdConv() + " validée");
                    
                    if (type.equals(Nommage.MSG_VALIDATION_JUR))
                            //formEnAttente.get(form.getIdConv()).setVerifJuridique(form.getVerifJuridique());
                    if (type.equals(Nommage.MSG_VALIDATION_ENS))
                            //formEnAttente.get(form.getIdConv()).setVerifJuridique(form.getVerifEnseignement());
                    if (type.equals(Nommage.MSG_VALIDATION_SCO))
                            //formEnAttente.get(form.getIdConv()).setVerifJuridique(form.getVerifScolarite());
                    
                    //si 3 vérifications sont valides, on envoie un msg au département d'enseignement
                    if(formulaireConfirmee(form.getIdConv())) 
                    {
                        ValidOk confirmation = new ValidOk(form.getIdConv(), form.getNumEtu(), Boolean.TRUE);
                        ObjectMessage om = session.createObjectMessage(confirmation);
                        om.setJMSType(Nommage.MSG_FORM_VALIDE);
                        mp2.send(om);
                    } else 
                    {
                        // On vérifie les validations manquantes
                        System.out.println("Validation Service Juridique : " + formEnAttente.get(form.getIdConv()).getVerifJuridique());
                        System.out.println("Validation Service Scolarité : " + formEnAttente.get(form.getIdConv()).getVerifScolarite());
                        System.out.println("Validation Départemnt d'enseignement : " + formEnAttente.get(form.getIdConv()).getVerifEnseignement());
                    }
                } 
                else 
                {
                    
                }
                
            }else {
                // nouvelle commande, on l'ajoute dans le dict
                formEnAttente.put(form.getIdConv(), new FormulaireEnValidation(form));
                System.out.println("--> Formulaire " + form.getIdConv() + " en attente.");
                
                if (type.equals(Nommage.MSG_DEPOT)){
                    
                    FormulaireEnValidation f = new FormulaireEnValidation(form);
                    formEnAttente.put(form.getIdConv(), f);
                    System.out.println("--> Formulaire " + form.getIdConv() + " en attente.");
                    
                    ObjectMessage om = session.createObjectMessage(f);
                    om.setJMSType(Nommage.MSG_DIFFUSION_AU_SERVICE);
                    mp1.send(om);
                } else {
                    formEnAttente.put(form.getIdConv(), new FormulaireEnValidation(form));
                    System.out.println("--> Formulaire " + form.getIdConv() + " en attente.");
                }
            }
        } catch (JMSException ex) 
        {
            System.out.println("exception");
            Logger.getLogger(SStage.class.getName()).log(Level.SEVERE, null, ex);
        }
/*        try {
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
*/
    }
}
