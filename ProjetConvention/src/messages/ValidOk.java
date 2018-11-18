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
    private String valConvention;

    public ValidOk(int idConv, int numEtu, String valConvention) {
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

    public String getValConvention() {
        return valConvention;
    }

    public void setValConvention(String valConvention) {
        this.valConvention = valConvention;
    }

    @Override
    public String toString() {
        return "ValidOk{" + "idConv=" + idConv + ", numEtu=" + numEtu + ", valConvention=" + valConvention + '}';
    }
    
}
