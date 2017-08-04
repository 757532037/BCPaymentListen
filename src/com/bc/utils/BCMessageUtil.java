package com.bc.utils;

import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class BCMessageUtil {

    	private static Log log = LogFactory.getLog(BCMessageUtil.class);
	/*
	 * 将bcpay报文解成map形式 key-value
	 */
	public static HashMap<String,String> parseBcpayMsg2Map(String msg){
		HashMap<String,String> returnMap = new HashMap<String,String>();
		StringTokenizer msgStoken = new StringTokenizer(msg,"|");
		String fieldArray[] = null;
		String str;
		boolean flag = false;
		String key = null;
		String value = null;
		while(msgStoken.hasMoreTokens()){
			str = msgStoken.nextToken();
			if(str.indexOf("SOH") != -1 || str.indexOf("EOH") != -1){
				continue;
			}else if(str.indexOf("EOM") != -1){
				flag = true;
				break;
			}
			fieldArray = str.split("=", 2);
			if(fieldArray.length >= 2 && fieldArray[1].length() > 0){
				key = fieldArray[0];
				value = fieldArray[1];
				returnMap.put(key, value);
			}else{
				key = fieldArray[0];
				value = null;
				returnMap.put(key, value);
			}
		}
		if(flag){
			log.debug("msgMap::" + returnMap);
			log.info("解包完成!");
		}else{
			log.info("解包未完成!");
		}
		return returnMap;
	}
	
	/**
	 * add the message tag,if the tag is exist,then replace the oldData with newData. 
	 * @param message
	 * @param tag
	 * @param newData
	 * @return
	 */
	public static String addMsgTag(String message,String tag, String newData){
		int start = -1;
		int end = -1;
		String msgEnd = "|EOM=|";
		int msgEndIndex = message.length() - msgEnd.length();
		
		try {
			tag = "|" + tag + "=";
			start = message.indexOf(tag);
			if (-1 == start) {
				//因为冲正报文中会带有原交易报文，|EOM=|结束字段会出现多次，在此处理
				if (message.indexOf(msgEnd) != msgEndIndex
						&& message.lastIndexOf(msgEnd) == msgEndIndex) {
					message = message.substring(0,message.lastIndexOf(msgEnd));
				} else {
					message = message.replace("|EOM=|", "");
				}
				message = message + tag + newData;
				message = message + "|EOM=|";
			}else{
				start += tag.length();
				end = message.indexOf("|", start);
				String data = message.substring(start, end).trim();
				message = message.replace(tag+data, tag + newData);
				//message = message + tag + newData;	
				//log.info("oldData="+tag+data+" newData="+ tag + newData);
			}
			
		} catch (Exception e) {
			log.error(e,e);
			return "";
		}
		return message;
	}
//	public static void main(String[] args) {
//	    String s = "|FIXSESSIONID=FIX.4.4:BCGW->1014|SOH=|VERS=1.00|MTYPE=CODEINFORESP|TRMID=wem21501|STOREID=wem215|CUSTID=1014|POSTIME=1488352316476|POSID=364|EOH=|SEQ=1488352316476|TIMEOUT=90|TRANSAMT=null|ORDERID=null|TRANSDATE=20170301|TRANSTIME=151154|BANKNAME=伯乔金融|RETCODE=0000000|RETCOMMENT=交易成功|BANKID=000001|ACTIVITY=广发O2O沃尔玛50元购物卡|PRODTYPE=10140533|DISCNTAMT=0.00|ATTENTION=权益验证通过；|GOODSINFO=获得沃尔玛50元购物卡|ACTTYPE=000003|EOM=|";
//	    
//	    System.out.println(GetValue(s, "MTYPE"));
//	}
	public static String GetValue(String message, String tag) {
		int start = -1;
		int end = -1;

		try {
			if (0 == message.length()) {
				log.error("Message not defined, from fixengin is empty.");
				return "";
			}
			if (0 == tag.length()) {
				log.error("Tag not defined");
				return "";
			} else
				tag = "|" + tag + "=";
			start = message.indexOf(tag);
			if (-1 == start) {
				return "";
			}
			start += tag.length();
			end = message.indexOf("|", start);
			if (-1 == end) {
				log.error("Badly formated message encountered!");
				return "";
			}
		} catch (Exception e) {
			log.error(e, e);
			return "";
		}

		return message.substring(start, end).trim();
	}
	
	public static String cutPassword(String message){
		
		String outMsg = message.toString();
		outMsg = outMsg.replaceAll("PAN=(\\d{6})\\d{0,99}(\\d{4})[|]SECONDTRACK=.", "PAN=$1****$2|SECONDTRACK=");
		outMsg = outMsg.replaceAll("SECONDTRACK=.*[|]TRANSAMT=.", "SECONDTRACK=|TRANSAMT=");
		outMsg = outMsg.replaceAll("PANPASSWD=.*[|]SECONDTRACK=.", "PANPASSWD=******|SECONDTRACK=");
		outMsg = outMsg.replaceAll("PASSWORD=.*[|]EOP=.", "PASSWORD=******|EOP=");
		outMsg = outMsg.replaceAll("PASSWORD=.*[|]STOREID=.", "PASSWORD=******|STOREID=");
		outMsg = outMsg.replaceAll("PASSWORD=.*[|]CUSTID=.", "PASSWORD=******|CUSTID=");
		return outMsg;
	}
}
