package com.bc.frame.obs.bcpay.advbank;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bc.frame.obs.IobServerAction;

public class Guangfao2oBank implements IobServerAction{

    private static Log log = LogFactory.getLog(IobServerAction.class);
    
    @Override
    public void notifyBank(String onMsg) {
	log.info(String.format("gf onMsg:[%s]", onMsg));
    }

}
