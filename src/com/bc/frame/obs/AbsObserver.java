package com.bc.frame.obs;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;

/**
 * 
 * @author 作者 :Administrator
 * @version 创建时间：2017-3-2 下午4:00:24
 * 类说明
 *用于配置需要监听的引擎
 */
public abstract class AbsObserver {

    List<IobServerAction> servers = new ArrayList<IobServerAction>();
    
    public void add(IobServerAction iob){
	this.servers.add(iob);
    } 
    
    public void remove(IobServerAction iob){
	this.servers.remove(iob);
    }
    
    public void notifyAll(String onMsg){
	for(IobServerAction iobServerAction :servers){
	    iobServerAction.notifyBank(onMsg);
	}
    }
    
    public abstract void startlisten() throws JMSException;
    
}
