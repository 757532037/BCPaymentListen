package com.bc.frame.obs;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.MessageListener;

import com.bc.frame.obs.bcpay.ListeningBC;
import com.bc.frame.obs.citicpay.ListeningCITIC;
import com.bc.utils.Tools;

public class Motloading{

    ListeningBC listeningBC;
    
    ListeningCITIC listeningCITIC;
   
    /**  
     *  设置listeningBC  
     * @return listeningBC listeningBC  
     */
    public void setListeningBC(ListeningBC listeningBC) {
        this.listeningBC = listeningBC;
    }

    /**  
     *  设置listeningCITIC  
     * @return listeningCITIC listeningCITIC  
     */
    public void setListeningCITIC(ListeningCITIC listeningCITIC) {
        this.listeningCITIC = listeningCITIC;
    }


    public void loadingListener() throws JMSException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
	
	listening(listeningBC);
	listening(listeningCITIC);
    }
 
    private void listening(AbsObserver listener) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, JMSException{
	for (Class<?> c : Tools.getAllAssignedClass(IobServerAction.class)) {
	    IobServerAction notify = (IobServerAction)c.newInstance();
	    listener.add(notify);
	}
	
	listener.startlisten();
    }
}
