/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Fouad El Ouaryaghli
 */
public class Formulaire implements Serializable{
    
   
    protected int idConv; 
    protected String nomEtu;
    protected String preEtu; 
    protected int numEtu;
    protected String nivEtu; 
    protected String dip;
    protected String nomAss;
    protected int numAss;
    protected String nomEnt; 
    protected int numEnt;
    protected GregorianCalendar dtDeb; 
    protected GregorianCalendar dtFin; 
    protected int paie;
    protected String resumeStage;

    public Formulaire(int idConv, String nomEtu, String preEtu, int numEtu, String nivEtu, String dip, String nomAss, int numAss, String nomEnt, int numEnt, GregorianCalendar dtDeb, GregorianCalendar dtFin, int paie, String resumeStage) {
        this.idConv = idConv;
        this.nomEtu = nomEtu;
        this.preEtu = preEtu;
        this.numEtu = numEtu;
        this.nivEtu = nivEtu;
        this.dip = dip;
        this.nomAss = nomAss;
        this.numAss = numAss;
        this.nomEnt = nomEnt;
        this.numEnt = numEnt;
        this.dtDeb = dtDeb;
        this.dtFin = dtFin;
        this.paie = paie;
        this.resumeStage = resumeStage;
    }

    public int getIdConv() {
        return idConv;
    }

    public void setIdConv(int idConv) {
        this.idConv = idConv;
    }

    public String getNomEtu() {
        return nomEtu;
    }

    public void setNomEtu(String nomEtu) {
        this.nomEtu = nomEtu;
    }

    public String getPreEtu() {
        return preEtu;
    }

    public void setPreEtu(String preEtu) {
        this.preEtu = preEtu;
    }

    public int getNumEtu() {
        return numEtu;
    }

    public void setNumEtu(int numEtu) {
        this.numEtu = numEtu;
    }

    public String getNivEtu() {
        return nivEtu;
    }

    public void setNivEtu(String nivEtu) {
        this.nivEtu = nivEtu;
    }

    public String getDip() {
        return dip;
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    public String getNomAss() {
        return nomAss;
    }

    public void setNomAss(String nomAss) {
        this.nomAss = nomAss;
    }

    public int getNumAss() {
        return numAss;
    }

    public void setNumAss(int numAss) {
        this.numAss = numAss;
    }

    public String getNomEnt() {
        return nomEnt;
    }

    public void setNomEnt(String nomEnt) {
        this.nomEnt = nomEnt;
    }

    public int getNumEnt() {
        return numEnt;
    }

    public void setNumEnt(int numEnt) {
        this.numEnt = numEnt;
    }

    public GregorianCalendar getDtDeb() {
        return dtDeb;
    }

    public void setDtDeb(GregorianCalendar dtDeb) {
        this.dtDeb = dtDeb;
    }

    public GregorianCalendar getDtFin() {
        return dtFin;
    }

    public void setDtFin(GregorianCalendar dtFin) {
        this.dtFin = dtFin;
    }

    public int getPaie() {
        return paie;
    }

    public void setPaie(int paie) {
        this.paie = paie;
    }

    public String getResumeStage() {
        return resumeStage;
    }

    public void setResumeStage(String resumeStage) {
        this.resumeStage = resumeStage;
    }

    @Override
    public String toString() {
        return "Formulaire{" + "idConv=" + idConv + ", nomEtu=" + nomEtu + ", preEtu=" + preEtu + ", numEtu=" + numEtu + ", nivEtu=" + nivEtu + ", dip=" + dip + ", nomAss=" + nomAss + ", numAss=" + numAss + ", nomEnt=" + nomEnt + ", numEnt=" + numEnt + ", dtDeb=" + dtDeb + ", dtFin=" + dtFin + ", paie=" + paie + ", resumeStage=" + resumeStage + '}';
    }
    
    
}
