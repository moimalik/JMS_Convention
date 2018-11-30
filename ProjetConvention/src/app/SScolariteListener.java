/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

/**
 *
 * @author malik
 */
public class SScolariteListener implements MessageListener{

    private final MessageProducer mp;
    private final Session session;

    public SScolariteListener(Session session, MessageProducer mp) {
        this.session = session;
        this.mp = mp;
        
    }
    @Override
    public void onMessage(Message message) {
        System.out.println(message.toString()); 
    }   
}
