package com.bc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpRequestUtil {
    
    private static Log log = LogFactory.getLog(HttpRequestUtil.class);
    /**
     * 
     * @Title: httpsPost
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return String 返回类型
     * @author yt.li
     */
    public static String httpsPost(String url, String postDataXML)
	    throws IOException {
	StringBuilder sb = new StringBuilder();
    	try {
    	    log.info(url+postDataXML);
            URL httpurl = new URL(url);
            URLConnection con = httpurl.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Pragma:", "no-cache");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "text/xml");

            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(new String(postDataXML.getBytes(),"utf-8"));
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(con
                    .getInputStream(),"utf-8"));
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                sb.append(line+"\r\n");
            }
    	    log.info(sb.toString());

	    return sb.toString();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	}
        return sb.toString();
    }
 public static void main(String[] args) throws Exception {
    String s = doGet("http://localhost:8080/GuangFao2o_security/gfo2o/not");
    System.out.println(s);
}
	/**
	 * 发送get请求获取结果集string
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url) throws Exception {
		URL localURL = new URL(url);
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		if (httpURLConnection.getResponseCode() >= 300) {
			throw new Exception(
					"HTTP Request is not success, Response code is "
							+ httpURLConnection.getResponseCode());
		}

		try {
			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}

		} finally {

			if (reader != null) {
				reader.close();
			}

			if (inputStreamReader != null) {
				inputStreamReader.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}

		}
		String str = resultBuffer.toString();
		return str;
	}
	
	 public static String GetRequest(String url) throws IllegalStateException, IOException {  
	        HttpClient client = new HttpClient();  
	        StringBuilder sb = new StringBuilder();  
	        InputStream ins = null;  
	        // Create a method instance.  
	        GetMethod method = new GetMethod(url);  
	        // Provide custom retry handler is necessary  
	        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,  
	                new DefaultHttpMethodRetryHandler(3, false));  
	        try {  
	            // Execute the method.  
	            int statusCode = client.executeMethod(method);  
	            System.out.println(statusCode);  
	            if (statusCode == HttpStatus.SC_OK) {  
	                ins = method.getResponseBodyAsStream();  
	                byte[] b = new byte[1024];  
	                int r_len = 0;  
	                while ((r_len = ins.read(b)) > 0) {  
	                    sb.append(new String(b, 0, r_len, method  
	                            .getResponseCharSet()));  
	                }  
	            } else {  
	                System.err.println("Response Code: " + statusCode);  
	            }  
	        } catch (HttpException e) {  
	            System.err.println("Fatal protocol violation: " + e.getMessage());  
	        } catch (IOException e) {  
	            System.err.println("Fatal transport error: " + e.getMessage());  
	        } finally {  
	            method.releaseConnection();  
	            if (ins != null) {  
	                ins.close();  
	            }  
	        }  
	        System.out.println(sb.toString());  
	        return sb.toString();
	    }

    public static String doPost(String url, Map map) {
	String jsonString = JSONObject.toJSONString(map);
	DefaultHttpClient client = new DefaultHttpClient();
	HttpPost post = new HttpPost(url);
	String response = null;
	try {
	    StringEntity s = new StringEntity(jsonString);
	    s.setContentEncoding("utf-8");
	    s.setContentType("application/json");
	    post.setEntity(s);
	    HttpResponse res = client.execute(post);
	    if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
		HttpEntity entity = res.getEntity();
		String result = EntityUtils.toString(res.getEntity());
		response = result;
	    }
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
	return response;
    }
}
