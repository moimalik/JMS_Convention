/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import messages.*;

/**
 *  service stage partie metier
 * 
 * @author Fouad El Ouaryaghli, Malik Belfodil
 */
public class SStageListener implements MessageListener {

    private final MessageProducer mp;
    private final Session session;
    
    static final HashMap<Integer, FormulaireEnValidation> formEnAttente = new HashMap();;

    public SStageListener(Session session, MessageProducer mp) {
        this.session = session;
        this.mp = mp;
    }
/**
         * verification des retour de formulaire
         * 
         * @param key id de pre-convention
         *  
         * @return boolean. 
    */
    private boolean formulaireConfirmee(int key) {
        
        if (formEnAttente.containsKey(key)) {
            if (formEnAttente.get(key).getVerifEnseignement() == EtatFormulaire.VALIDEE &&
                    formEnAttente.get(key).getVerifJuridique()== EtatFormulaire.VALIDEE &&
                    formEnAttente.get(key).getVerifScolarite()== EtatFormulaire.VALIDEE)
            return true;
        }
        return false;
    }
    /**
         * reception, realisation metier et expedition
         * 
         * @param message messge a traiter 
         *  
         * @return void. 
    */
    @Override
    public void onMessage(Message msg) {
        try 
        {   
            System.out.println("---------------------");
            Queue source = (Queue) msg.getJMSDestination();
            String queueName = source.getQueueName();
            
            if (queueName.equalsIgnoreCase(Nommage.QUEUE_VALIDATION)){
                FormulaireEnValidation form;
                String type;

                if (msg instanceof ObjectMessage) 
                {
                    ObjectMessage om = (ObjectMessage) msg;
                    type = om.getJMSType();
                    System.out.println(type);
                    Object obj = om.getObject();
                    
                    if (obj instanceof Formulaire) 
                    {  
                        form = (FormulaireEnValidation) obj;
                        System.out.println("FormV");
                        
                        //reception d'un formulaire en validation depuis les différents services
                        if (formEnAttente.containsKey(form.getIdConv())) 
                        {
                            // Formaulaire deja recue

                            System.out.println("--> Formulaire " + form.getIdConv() + " recue");

                            if (type.equals(Nommage.MSG_VALIDATION_JUR))
                                    formEnAttente.get(form.getIdConv()).setVerifJuridique(form.getVerifJuridique());
                            if (type.equals(Nommage.MSG_VALIDATION_ENS))
                                    formEnAttente.get(form.getIdConv()).setVerifEnseignement(form.getVerifEnseignement());
                            if (type.equals(Nommage.MSG_VALIDATION_SCO))
                                    formEnAttente.get(form.getIdConv()).setVerifScolarite(form.getVerifScolarite());

                            if (formulaireConfirmee(form.getIdConv())) 
                            {

                                System.out.println("--> Formulaire de pré-convention " + form.getIdConv() + " à reçu toutes les validations !!!");

                                ValidOk confirmation = new ValidOk(form.getIdConv(), form.getNumEtu(), Boolean.TRUE);
                                
                                om = session.createObjectMessage(confirmation);
                                om.setJMSType(Nommage.MSG_FORM_VALIDE);
                                mp.send(om);
                            } else {

                                // On vérifie les validations manquantes
                                System.out.println("Validation Service Juridique : " + formEnAttente.get(form.getIdConv()).getVerifJuridique());
                                System.out.println("Validation Service Scolarité : " + formEnAttente.get(form.getIdConv()).getVerifScolarite());
                                System.out.println("Validation Département d'enseignement : " + formEnAttente.get(form.getIdConv()).getVerifEnseignement());

                            }
                        }
                    }
                }
            }
            
            if (queueName.equalsIgnoreCase(Nommage.QUEUE_DEPOT)){
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
                        System.out.println("Form");
                        
                        if (type.equals(Nommage.MSG_DEPOT)){

                            FormulaireEnValidation f = new FormulaireEnValidation(form);
                            formEnAttente.put(form.getIdConv(), f);
                            System.out.println("--> Formulaire " + form.getIdConv() + " en attente.");
                            
                            formEnAttente.entrySet().forEach((fo) -> {
                                System.out.println(fo.getKey());
                            });
                            System.out.println("-----------------------------------");

                            om = session.createObjectMessage(f);
                            om.setJMSType(Nommage.MSG_DIFFUSION_AU_SERVICE);
                            mp.send(om);
                            System.out.println("--> Formulaire " + form.getIdConv() + " diffusé aux différents service.");

                        }
                    }
                }
            }
        } catch (JMSException ex) 
        {
            System.out.println("exception");
            Logger.getLogger(SStage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
