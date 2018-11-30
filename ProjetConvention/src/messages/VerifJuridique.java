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
public class VerifJuridique extends Formulaire{
    
    private Boolean validJuridique;

    public VerifJuridique(Boolean validJuridique, Formulaire f) {
        super(f.getIdConv(), f.nomEtu, f.preEtu, f.numEtu, f.nivEtu, f.dip, f.nomAss, f.numAss, f.nomEnt, f.numEnt, f.dtDeb, f.dtFin, f.paie, f.resumeStage);
        this.validJuridique = validJuridique;
    }

    public Boolean getvalidJuridique() {
        return validJuridique;
    }

    public void setvalidJuridique(Boolean validJuridique) {
        this.validJuridique = validJuridique;
    }

    @Override
    public String toString() {
        return super.toString() + "validJuridique" +  validJuridique ;
    }   
        
}
