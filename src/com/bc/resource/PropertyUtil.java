package com.bc.resource;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * 
 * @author
 * 
 */
public class PropertyUtil {
	private static final String BUNDLE_NAME = "resource.";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private PropertyUtil() {

	}
            public static String getString(String key) {
        	String url = "";
        	try {
        	    url = new String(RESOURCE_BUNDLE.getString(key).getBytes(
        		    "iso-8859-1"), "UTF-8");
        	} catch (MissingResourceException e) {
        	    e.printStackTrace();
        	    return null;
        	} catch (UnsupportedEncodingException e) {
        	    // TODO Auto-generated catch block
        	    e.printStackTrace();
        	    return null;
        	}
        	return url;
            }

}
