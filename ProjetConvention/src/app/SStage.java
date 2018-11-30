/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
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
  
    private MessageConsumer mc;
    private MessageProducer mp1;
    private MessageProducer mp2;
    
    private final HashMap<Integer, FormulaireEnValidation> formEnAttente;

    public SStage() {
        this.formEnAttente = new HashMap();
    }
    
    
    private void setProducerConsumer() {

        try {
            
            // recuperation des destinations
            Destination formEmis = (Destination) namingContext.lookup(Nommage.TOPIC_FICHE_CONVENTION);
            Destination formValides = (Destination) namingContext.lookup(Nommage.QUEUE_VALIDATION);
            Destination formConfirmes = (Destination) namingContext.lookup(Nommage.QUEUE_CONFIRMATION);
            System.out.println("Destination lookup done.");

            // creation des consommateurs et du producteur
            mc = session.createConsumer(formValides);
            mp1 = session.createProducer(formEmis);
            mp2 = session.createProducer(formConfirmes);

            // Quel MessageProducer doit on choisir
            SStageListener fl = new SStageListener(session, mp1);
            SStageListener f2 = new SStageListener(session, mp2);
            mc.setMessageListener(fl);

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
            FormulaireEnValidation form;
            String type;
            if (msg instanceof ObjectMessage) 
            {
                ObjectMessage om = (ObjectMessage) msg;
                Object obj = om.getObject();
                if (obj instanceof FormulaireEnValidation) 
                {
                    form = (FormulaireEnValidation) obj;
                    type = om.getJMSType();
                } else 
                {
                    return;
                }
            } else 
            {
                return;
            }

            if (formEnAttente.containsKey(form.getIdConv())) 
            {
                // Formaulaire deja recue
                System.out.println("--> Formulaire " + form.getIdConv() + " recue");

                if (formulaireConfirmee(form.getIdConv())) 
                {
                    
                    System.out.println("--> Formulaire de pré-convention " + form.getIdConv() + " validée");
                    
                    if (type.equals(Nommage.MSG_VALIDATION_JUR))
                            formEnAttente.get(form.getIdConv()).setVerifJuridique(form.getVerifJuridique());
                    if (type.equals(Nommage.MSG_VALIDATION_ENS))
                            formEnAttente.get(form.getIdConv()).setVerifJuridique(form.getVerifEnseignement());
                    if (type.equals(Nommage.MSG_VALIDATION_SCO))
                            formEnAttente.get(form.getIdConv()).setVerifJuridique(form.getVerifScolarite());
                    
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
                } else 
                {
                    // nouvelle commande, on l'ajoute dans le dict
                    cmdsEnAttente.put(cmd.getNumCommande(), cmd);
                    System.out.println("--> Commande " + cmd.getNumCommande() + " mise en attente.");
                }
            }
        } catch (JMSException ex) 
        {
            Logger.getLogger(SStage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public static void main(String[] args) throws Exception {

        SStage serviceStage = new SStage();
        serviceStage.initJMS();
        serviceStage.setProducerConsumer();
        serviceStage.startJMS();
        System.out.println("*** Service de stage démarré. ***");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("Appuyez sur 'Q' pour quitter.");
        } while (!br.readLine().equalsIgnoreCase("Q"));
        serviceStage.closeJMS();
    }
}
