/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 *
 * @author Fouad El Ouaryaghli
 */
public class Nommage {
    
    public final static String FABRIQUE_CONNEXIONS = "jms/__defaultConnectionFactory";
    
    public final static String TOPIC_FICHE_CONVENTION = "jms/FicheConv";
    public final static String QUEUE_VALIDATION = "jms/Validation";
    public final static String QUEUE_CONFIRMATION = "jms/Confirmation";
    
    public final static String MSG_STOCK = "STOCK";
    public final static String MSG_FACTURATION = "FACTURATION";
    public final static String MSG_CMD_ANNULEE = "CMD_ANNULEE";
    public final static String MSG_FORM_VALIDE = "FORM_VALIDE";
    
}
