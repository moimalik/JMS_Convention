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
    
    public FormulaireEnValidation(Formulaire f) {
        super(f.getIdConv(), f.nomEtu, f.preEtu, f.numEtu, f.nivEtu, f.dip, f.nomAss, f.numAss, f.nomEnt, f.numEnt, f.dtDeb, f.dtFin, f.paie, f.resumeStage);
    }
    
}
