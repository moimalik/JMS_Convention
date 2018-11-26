/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.io.Serializable;

/**
 *
 * @author malik
 */
public class ValidOk implements Serializable{
    
    protected int idConv; 
    protected int numEtu;
    private Boolean valConvention;

    public ValidOk(int idConv, int numEtu, Boolean valConvention) {
        this.idConv = idConv;
        this.numEtu = numEtu;
        this.valConvention = valConvention;
    }

    public int getIdConv() {
        return idConv;
    }

    public void setIdConv(int idConv) {
        this.idConv = idConv;
    }

    public int getNumEtu() {
        return numEtu;
    }

    public void setNumEtu(int numEtu) {
        this.numEtu = numEtu;
    }

    public Boolean getValConvention() {
        return valConvention;
    }

    public void setValConvention(Boolean valConvention) {
        this.valConvention = valConvention;
    }

    @Override
    public String toString() {
        return "ValidOk{" + "idConv=" + idConv + ", numEtu=" + numEtu + ", valConvention=" + valConvention + '}';
    }
    
}
