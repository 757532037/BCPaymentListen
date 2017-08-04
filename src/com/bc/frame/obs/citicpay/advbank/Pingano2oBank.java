package com.bc.frame.obs.citicpay.advbank;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bc.frame.obs.IobServerAction;

public class Pingano2oBank implements IobServerAction{

    private static Log log = LogFactory.getLog(IobServerAction.class);
    
    @Override
    public void notifyBank(String onMsg) {
	log.info(String.format("pan onMsg:[%s]", onMsg));
    }

}
