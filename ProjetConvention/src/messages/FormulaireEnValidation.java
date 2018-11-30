/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.util.Date;

/**
 *
 * @author Fouad El Ouaryaghli
 */
public class FormulaireEnValidation extends Formulaire{
    
    private EtatFormulaire verifScolarite;
    private EtatFormulaire verifEnseignement;
    private EtatFormulaire verifJuridique; 
    
    public FormulaireEnValidation(Formulaire f) {
        super(f.getIdConv(), f.nomEtu, f.preEtu, f.numEtu, f.nivEtu, f.dip, f.nomAss, f.numAss, f.nomEnt, f.numEnt, f.dtDeb, f.dtFin, f.paie, f.resumeStage);
        verifScolarite = EtatFormulaire.EN_COURS_DE_TRAITEMENT;
        verifEnseignement = EtatFormulaire.EN_COURS_DE_TRAITEMENT;
        verifJuridique = EtatFormulaire.EN_COURS_DE_TRAITEMENT;
    }

    public EtatFormulaire getVerifScolarite() {
        return verifScolarite;
    }

    public void setVerifScolarite(EtatFormulaire verifScolarite) {
        this.verifScolarite = verifScolarite;
    }

    public EtatFormulaire getVerifEnseignement() {
        return verifEnseignement;
    }

    public void setVerifEnseignement(EtatFormulaire verifEnseignement) {
        this.verifEnseignement = verifEnseignement;
    }

    public EtatFormulaire getVerifJuridique() {
        return verifJuridique;
    }

    public void setVerifJuridique(EtatFormulaire verifJuridique) {
        this.verifJuridique = verifJuridique;
    }
    
}
