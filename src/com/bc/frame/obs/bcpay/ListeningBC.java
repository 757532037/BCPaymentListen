package com.bc.frame.obs.bcpay;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bc.frame.obs.AbsObserver;
import com.bc.utils.BCMessageUtil;

/**
 * 
 * @author 作者 :Administrator
 * @version 创建时间：2017-3-2 下午4:58:52
 * 类说明
 *伯乔监听服务
 */
public class ListeningBC extends AbsObserver implements MessageListener{

    private static Log log = LogFactory.getLog(ListeningBC.class);

    public Topic pub_topic = null;
    public Topic rec_topic = null;
    public Session session = null;
    public MessageConsumer consumer = null;
    public MessageProducer producer= null;
    public String userName;
    public String password; 
    public String brokerURL;
    public String rec_topic_recv;
    public String pub_topic_send;
    public Connection connection;
    
    /**  
     *  获取userName  
     * @return userName userName  
     */
    public    String getUserName() {
        return userName;
    }

    /**  
     *  设置userName  
     * @return userName userName  
     */
    public   void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**  
     *  获取password  
     * @return password password  
     */
    public String getPassword() {
        return password;
    }

    /**  
     *  设置password  
     * @return password password  
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**  
     *  获取brokerURL  
     * @return brokerURL brokerURL  
     */
    public String getBrokerURL() {
        return brokerURL;
    }

    /**  
     *  设置brokerURL  
     * @return brokerURL brokerURL  
     */
    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    /**  
     *  获取rec_topic_recv  
     * @return rec_topic_recv rec_topic_recv  
     */
    public String getRec_topic_recv() {
        return rec_topic_recv;
    }

    /**  
     *  设置rec_topic_recv  
     * @return rec_topic_recv rec_topic_recv  
     */
    public void setRec_topic_recv(String rec_topic_recv) {
        this.rec_topic_recv = rec_topic_recv;
    }

    /**  
     *  获取pub_topic_send  
     * @return pub_topic_send pub_topic_send  
     */
    public String getPub_topic_send() {
        return pub_topic_send;
    }

    /**  
     *  设置pub_topic_send  
     * @return pub_topic_send pub_topic_send  
     */
    public void setPub_topic_send(String pub_topic_send) {
        this.pub_topic_send = pub_topic_send;
    }

    public void startlisten() throws JMSException{
	ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName, password, brokerURL);
	connection = factory.createConnection();
	session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	rec_topic = session.createTopic(rec_topic_recv);
	pub_topic = session.createTopic(pub_topic_send);
	consumer = session.createConsumer(rec_topic);
	consumer.setMessageListener(this);
	connection.start();
//	producer = session.createProducer(pub_topic);
//	TextMessage textMessage = null;
//	String onMsg = "|FIXSESSIONID=FIX.4.4:BCGW->1014|SOH=|VERS=1.00|MTYPE=CODEINFOREQ" +
//			"|POSTIME=1488211694117|TRMID=wem21501|POSID=2088|CUSTID=1014|" +
//			"CUSTNAME=沃尔玛|CASHIERID=wem21501|STOREID=wem215|SEQ=1488211694117|" +
//			"EOH=|TIMEOUT=90|PAN=06566702523|SECONDTRACK=|BANKNAME=伯乔金融|EOM=|";
//	textMessage = session.createTextMessage(String.valueOf(onMsg));
//	producer.send(textMessage);
	log.info("connect success ...");
    }
    
    private enum MTYP {
	DPREQ, CHECKACCOUNTREQ, CANCELREQ, CARDINFOREQ, 
	REFUNDREQ, RETRIEVETRANSACTIONREQ, LOGIN, LOGOUT, 
	CHANGEPASSWD, DOWNLOADFILE, UPLOADFILE, PRODLISTREQ,
	AUTOREVERSEREQ, POSLOGINREQ,POSLOGOUTREQ, ITEMLISTREQ, 
	ITEMLISTRESP, MONITORTESTREQ, MONITORTESTESP, CODEINFOREQ,
	RESETCASHREQ,QUERYSTATUS,DPRESP,CANCELRESP,AUTOREVERSERESP
    }
    
    @Override
    public void onMessage(Message arg0) {
	try {
	    String onMsg = String.valueOf(getText(arg0));
	    log.info("Listening on:\t"+BCMessageUtil.cutPassword(onMsg));
	    MTYP mtyp = null;
	    try {
		mtyp = MTYP.valueOf(BCMessageUtil.GetValue(onMsg, "MTYPE"));
	    } catch (Exception e) {
		mtyp = MTYP.DPREQ;
	    }
	    switch (mtyp) {
	    case DPRESP://验码
		
	    case CANCELRESP://撤销
		
	    case AUTOREVERSERESP://冲正
		
		this.notifyAll(String.valueOf(getText(arg0)));
		break;
		
	    default:
		break;
	    }
	} catch (JMSException e) {
	    e.printStackTrace();
	}
    }

     Object getText(Message message) throws JMSException{
	return ((TextMessage)message).getText();
    }
    
}
