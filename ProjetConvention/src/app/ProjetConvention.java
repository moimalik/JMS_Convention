/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import app.DepotFormulaire;
import javax.jms.JMSException;
import javax.naming.NamingException;

/**
 *
 * @author Fouad El Ouaryaghli
 */
public class ProjetConvention {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        
        DepotFormulaire dp = new DepotFormulaire();
        SStage st = new SStage();
        SScolarite ss = new SScolarite();
        SEnseignement se = new SEnseignement();
        SJuridique sj = new SJuridique();
        
        dp.initJMS();
        st.initJMS();
        ss.initJMS();
        se.initJMS();
        sj.initJMS();
        
        dp.setProducerConsumer();
        st.setProducerConsumer();
        ss.setProducerConsumer();
        se.setProducerConsumer();
        sj.setProducerConsumer();
        
        dp.startJMS();
        st.startJMS();
        ss.startJMS();
        se.startJMS();
        sj.startJMS();
        
        System.out.println("*** Service de dépot des pré-convention a démarré. ***");
        System.out.println("*** Service de stage a démarré. ***");
        System.out.println("*** Service de scolarité a démarré. ***");
        System.out.println("*** Service du département d'enseignement a démarré. ***");
        System.out.println("*** Service juridique a démarré. ***");
        
        dp.runSimu();
        
        dp.closeJMS();
    }
    
    
}
