/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.util.Date;

/**
 *
 * @author malik
 */
public class VerifEns extends Formulaire{
    
    private Boolean validEns;

    public VerifEns(Boolean validEns, int idConv, String nomEtu, String preEtu, int numEtu, String nivEtu, String dip, String nomAss, int numAss, String nomEnt, int numEnt, Date dtDeb, Date dtFin, int paie, String resumeStage) {
        super(idConv, nomEtu, preEtu, numEtu, nivEtu, dip, nomAss, numAss, nomEnt, numEnt, dtDeb, dtFin, paie, resumeStage);
        this.validEns = validEns;
    }

    public Boolean getvalidJuridique() {
        return validEns;
    }

    public void setvalidJuridique(Boolean validJuridique) {
        this.validEns = validJuridique;
    }

    @Override
    public String toString() {
        return super.toString() + "validEns" +  validEns ;
    }   
        
}