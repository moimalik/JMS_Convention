/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import com.google.gson.Gson;
import fr.miage.toulouse.m2.eai.clientrest.metiersiren.SirenPOJO;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import messages.*;

/**
 *  service juridique partie metier
 * 
 * @author Fouad El Ouaryaghli, Malik Belfodil
 */
public class SJuridiqueListener implements MessageListener {
    
    private final MessageProducer mp;
    private final Session session;

    public SJuridiqueListener(Session session, MessageProducer mp) {
        this.session = session;
        this.mp = mp;
    }
    /**
         * reception realisation metier et expedition
         * 
         * @param message message traité
         *  
         * @return void. 
    */
    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("------------------------");
        
            Topic source = (Topic) message.getJMSDestination();

            // System.out.println("MSG RECU " + source.getTopicName());
            String topicName = source.getTopicName();
            System.out.println(topicName);

            if (topicName.equalsIgnoreCase(Nommage.TOPIC_FICHE_CONVENTION)) {

                if (message instanceof ObjectMessage) {
                    ObjectMessage om = (ObjectMessage) message;
                    Object obj = om.getObject();
                    if (obj instanceof Formulaire) {
                        Formulaire form = (Formulaire) obj;
                        System.out.println("Formulaire n° " + form.getIdConv()+ " reçue ");
                        
                        FormulaireEnValidation verif = new FormulaireEnValidation(form);
                        
                        //////////////PARTIE METIER///////////////
                        boolean vEntreprise = metierInsee(String.valueOf(verif.getNumEnt()) ,verif.getNomEnt()) ;
                        boolean vAssurance = metierAssurance(verif.getNomEtu(), verif.getNomAss(), verif.getNumAss(), verif.getDtDeb(), verif.getDtFin()) ;
                        boolean vDate = metierDate(verif.getDtDeb(),verif.getDtFin()) ;
                        boolean vRemuneration = metierRemuneration(verif.getPaie()) ;
                        
                        /*System.out.println("result vent " + vEntreprise);
                        System.out.println("result ass " + vAssurance);
                        System.out.println("result date " + vDate);
                        System.out.println("result rem " + vRemuneration);*/
                        if(vEntreprise && vAssurance && vDate && vRemuneration){
                        //if(true){
                            verif.setVerifJuridique(EtatFormulaire.VALIDEE);
                        }else{
                            verif.setVerifJuridique(EtatFormulaire.REFUSEE);
                        }
                        
                        // envoi de la réponse de la banque
                        ObjectMessage msg = session.createObjectMessage(verif);
                        msg.setJMSType(Nommage.MSG_VALIDATION_JUR);
                        mp.send(msg);
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(SJuridiqueListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /**
         * Retourne un boolean qui verifie l'entreprise dans la base INSEE
         * 
         * @param siren siren de l'entreprise
         * @param RS nom de l'entreprise
         * 
         * @return boolean. 
    */
    public boolean metierInsee(String siren, String RS ){
        Gson gson = new Gson();

        // TOKEN BEARER a récuperer sur INSEE
        String token = "Bearer 87faafeb-b34f-39d4-8cc0-cb9e7a15a8d9";
        // URI Service INSEE
        String uri = "http://data.opendatasoft.com/api/records/1.0/search/?dataset=sirene%40public";

        // a ajuster selon requete voir mode emploi INSEE
        String query = "&lang=fr&rows=1";

        // SIREN a chercher
        
        // Pour info siren = "552100554"; //PIGEOT.

        Client client = ClientBuilder.newClient();
        WebTarget wt = client.target(uri + "&q=" + siren + query);

        //WebResource webResource = client.resource(uri + siren + query);
        System.out.println("uri appel: " + uri + "&q=" + siren + query);

        Invocation.Builder invocationBuilder = wt.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        String reponse = response.readEntity(String.class);
        
        try{
            // Convertisseur JSON
            SirenPOJO model = gson.fromJson(reponse, SirenPOJO.class);
            System.out.println("Résultat: " + response.getStatus());
            System.out.println("Raison sociale : " + model.getRecords()[0].getFields().getL1_normalisee() + ", Date création entité : " + model.getRecords()[0].getFields().getDcren() + ", Activité : " + model.getRecords()[0].getFields().getActivite());
            if (RS.equalsIgnoreCase(model.getRecords()[0].getFields().getL1_normalisee())){
                return true; 
            }else{
                return false;
            }
        }catch (ArrayIndexOutOfBoundsException ex){
            return false;
        }
    }
    /**
         * Retourne un boolean qui verifie si l'assurance est OK
         * 
         * @param nom de l'etudiant, numero et nom assurance et date de stage 
         * @param nomAss nom assurance
         * @param numAss numero assurance
         * @param debut debut de stage
         * @param fin debut de stage
         * @return boolean. 
    */
    public boolean metierAssurance(String nom,String nomAss, int numAss, Calendar debut, Calendar fin  ){
        // aucune api pour verifier les assurances
        Scanner sc = new Scanner(System.in);
        System.out.println("Nom "+nom+" Assurance "+nomAss+" N° Assurance "+numAss+" Stage Du "+debut.toString()+" au "+fin.toString());
        double val = Math.random();
        if (val < 0.9) {
            return true;
        } else {
            return false;
        }
        
    }
    /**
         * Retourne un boolean qui verifie si la remuneration est OK
         * 
         * @param salaire le salaire 
         *  
         * @return boolean. 
    */
    public boolean metierRemuneration(int salaire ){
        if (salaire > 500){
            return true;
        }else{
            return false;
        }
    }
    /**
         * Retourne un boolean qui verifie si les dates sont OK
         * 
         * @param debut debut de stage 
         * @param fin fin de stage
         *  
         * @return boolean. 
    */
    public boolean metierDate(GregorianCalendar debut, GregorianCalendar fin ){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Math.abs(debut.getTimeInMillis()-fin.getTimeInMillis()));
        int m = c.get(Calendar.MONTH);
        if( (debut.compareTo(fin) < 0)){
            if(m <= 6){
                if ((debut.get(Calendar.MONTH) < 9) && (fin.get(Calendar.MONTH)> 9) && 
                    ((fin.get(Calendar.YEAR) - debut.get(Calendar.YEAR)) <=1)
                        ){
                    return false;
                }else{
                    return true;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
        
}
