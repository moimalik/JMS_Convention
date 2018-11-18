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
public class VerifScolarite extends Formulaire{
    
    private Boolean validScolarite;

    public VerifScolarite(Boolean validScolarite, int idConv, String nomEtu, String preEtu, int numEtu, String nivEtu, String dip, String nomAss, int numAss, String nomEnt, int numEnt, Date dtDeb, Date dtFin, int paie, String resumeStage) {
        super(idConv, nomEtu, preEtu, numEtu, nivEtu, dip, nomAss, numAss, nomEnt, numEnt, dtDeb, dtFin, paie, resumeStage);
        this.validScolarite = validScolarite;
    }

    public Boolean getValidScolarite() {
        return validScolarite;
    }

    public void setValidScolarite(Boolean validScolarite) {
        this.validScolarite = validScolarite;
    }

    @Override
    public String toString() {
        return super.toString() + "validScolarit" +  validScolarite ;
    }   
        
}
