/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import messages.EtatFormulaire;
import messages.Formulaire;
import messages.FormulaireEnValidation;
import messages.ValidOk;
import org.apache.jasper.tagplugins.jstl.ForEach;

/**
 *
 * @author Fouad El Ouaryaghli
 */
public class SStage extends ClientJMS{
  
    private MessageConsumer mc1;
    private MessageConsumer mc2;
    private MessageProducer mp1;
    private MessageProducer mp2;
    
    private final HashMap<Integer, FormulaireEnValidation> formEnAttente;

    public SStage() {
        super();
        this.formEnAttente = new HashMap();
    }
    
    
    void setProducerConsumer() {

        try {
            
            // recuperation des destinations
            Destination formRecu = (Destination) namingContext.lookup(Nommage.QUEUE_DEPOT);
            Destination formEmis = (Destination) namingContext.lookup(Nommage.TOPIC_FICHE_CONVENTION);
            Destination formValides = (Destination) namingContext.lookup(Nommage.QUEUE_VALIDATION);
            Destination formConfirmes = (Destination) namingContext.lookup(Nommage.QUEUE_CONFIRMATION);
            System.out.println("Destination lookup done.");

            // creation des consommateurs et du producteur
            mc1 = session.createConsumer(formRecu);
            mc2 = session.createConsumer(formValides);
            mp1 = session.createProducer(formEmis);
            mp2 = session.createProducer(formConfirmes);

            // Quel MessageProducer doit on choisir
            //SStageListener fl = new SStageListener(session, mp1);
            //SStageListener f2 = new SStageListener(session, mp2);
            mc1.setMessageListener(new SScolariteListener(session, mp1));

        } catch (JMSException | NamingException ex) {
            Logger.getLogger(ex.getMessage());
        }
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
    private void processMessage(Message msg) {

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
    }
        
    public static void main(String[] args) throws Exception {

        boolean theEnd = false;
        
        SStage serviceStage = new SStage();
        serviceStage.initJMS();
        
        System.out.println("1");
        serviceStage.setProducerConsumer();
        
        
        System.out.println("2");
        serviceStage.startJMS();
        
        System.out.println("3");
        System.out.println("*** Service de stage démarré. ***");
        
        
        do {
            Message msg = serviceStage.mc1.receive();

            System.out.println("----------");
            serviceStage.processMessage(msg);
            
        } while (!theEnd);
        serviceStage.closeJMS();
    }
}
