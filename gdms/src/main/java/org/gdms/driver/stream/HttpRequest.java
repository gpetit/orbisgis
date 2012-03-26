package org.gdms.driver.stream;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

/**
 *
 * Handle HTTP requests
 *
 * @author Vincent Dépériers
 */


public class HttpRequest {

    private String m_encoding = "UTF-8";
    private String m_userAgent = "HttpComponents/1.1";
    private String m_host;
    private String m_target;
    private HashMap<String, String> m_parameters = new HashMap<String, String>();
    private int m_port = 80;
    private byte[] m_data; 
    private String m_ContentType;
    private HttpParams m_http_params;

    public HttpRequest(){
    	this.m_http_params = new SyncBasicHttpParams();
        HttpProtocolParams.setVersion(this.m_http_params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(this.m_http_params, this.m_encoding);
        HttpProtocolParams.setUserAgent(this.m_http_params, this.m_userAgent);
        HttpProtocolParams.setUseExpectContinue(this.m_http_params, true);
    }
    
    //getter m_url
    public String getHost() {
        return this.m_host;
    }

    //setter m_url
    public void setHost(String host) {
        this.m_host = host;
    }

    //getter m_port
    public int getPort() {
        return this.m_port;
    }

    //setter m_port
    public void setPort(int port) {
        this.m_port = port;
    }

    public String getTarget() {
        return this.m_target;
    }

    public void setTarget(String target) {
        this.m_target = target;
    }

    public void addParameter(String parameter, String value) throws Exception {
        if (this.m_parameters != null) {
        	this.m_parameters.put(parameter, value);
        }
    }

    public void removeParameter(String parameter) throws Exception {
        if (this.m_parameters != null) {
        	this.m_parameters.remove(parameter);
        }
    }

    public void clearParameter(){
    	this.m_parameters.clear();
    }
    
    public String getDataAsString() throws Exception {
        return new String(this.m_data);
    }
    
    public byte[] getDataAsByteArray() throws Exception {
        return m_data;
    }
    
    public String getContentType() {
    	return m_ContentType;
    }
    
    public void setUserAgent(String userAgent){
    	this.m_userAgent = userAgent;
    	HttpProtocolParams.setUserAgent(this.m_http_params, this.m_userAgent);
    }
    
    public String getUserAgent(){
    	return this.m_userAgent;
    }
    
    public void setEncoding(String encoding){
    	this.m_encoding = encoding;
    	HttpProtocolParams.setContentCharset(this.m_http_params, this.m_encoding);
    }
    
    public String getEncoding() {
    	return this.m_encoding;
    }
    
    
    public void GETRequest() throws Exception {

    	HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpRequestInterceptor[]{
                // Required protocol interceptors
                new RequestContent(),
                new RequestTargetHost(),
                // Recommended protocol interceptors
                new RequestConnControl(),
                new RequestUserAgent(),
                new RequestExpectContinue()});
        

        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

        HttpContext context = new BasicHttpContext(null);
        HttpHost host = new HttpHost(this.m_host, this.m_port);

        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        ConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();

        context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);
        
        try {
            if (!conn.isOpen()) {
                Socket socket = new Socket(host.getHostName(), host.getPort());
                conn.bind(socket, m_http_params);
            }
            
            BasicHttpRequest request = new BasicHttpRequest("GET", buildRequest());

            request.setParams(this.m_http_params);
            httpexecutor.preProcess(request, httpproc, context);
            HttpResponse response = httpexecutor.execute(request, conn, context);
            response.setParams(this.m_http_params);
            httpexecutor.postProcess(response, httpproc, context);
            
            //Set response as byte array
            m_data = EntityUtils.toByteArray(response.getEntity());
            System.out.println(response.getStatusLine().getStatusCode());
            
            //Check reponse type
            Header[] headers = response.getHeaders("Content-Type"); 
            if(headers.length > 0)
            	this.m_ContentType = headers[0].getValue();
            System.out.println(headers[0].getValue());
            
            if (!connStrategy.keepAlive(response, context)) {
                conn.close();
            }
        } catch (UnknownHostException e){
        	throw new Exception("HttpRequest::GETRequest() - Unknow host : " + e.getMessage());
        } catch (IOException e){
        	throw new Exception("HttpRequest::GETRequest() - " + e.getMessage()); 
        } finally {
            conn.close();
        }
    }

    private String buildRequest() {
        String request = this.m_target;
        boolean first = true;

        for (String parameter : this.m_parameters.keySet()) {
            if (first) {
                request += "?" + parameter + "=" + this.m_parameters.get(parameter);
                first = false;
            } else {
                request += "&" + parameter + "=" + this.m_parameters.get(parameter);
            }
        }
        
        return request;
    }
}