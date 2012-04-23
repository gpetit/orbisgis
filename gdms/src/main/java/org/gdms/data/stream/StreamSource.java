package org.gdms.data.stream;

import java.io.Serializable;

/**
 * Class that contains the information to identify a stream
 *
 * @author Vincent Dépériers
 */
public class StreamSource implements Serializable {

    private static final long serialVersionUID = 0L;
    private String m_host;
    private int m_port;
    private String m_layerName;
    //private String schemaName;
    private String m_imageFormat;
    private String m_user;
    private String m_password;
    private String m_prefix;
    private String m_srs;
    
    
    /**
     * Creat a new Stream Source with the host, the port,the name of the layer and the prefix
     * @param host
     * @param port
     * @param layerName
     * @param prefix 
     */
    public StreamSource(String host, int port, String layerName, String prefix) {
        this(host, port, layerName, prefix, "", "", "", "");
    }
     
    /**
     * Creat a new Stream Source with the host, the port,the name of the layer , 
     * the prefix,the format of the image and the srs
     * @param host
     * @param port
     * @param layerName
     * @param prefix
     * @param imageFormat
     * @param srs 
     */
    public StreamSource(String host, int port, String layerName, String prefix,
            String imageFormat, String srs) {
        this(host, port, layerName, prefix, imageFormat, srs, "", "");
    }
    
   /**
    * Creat a new Stream Source with the host, the port,the name of the layer , 
    * the prefix,the format of the image ,the srs,the user and the password if 
    * necessairy
    * @param host
    * @param port
    * @param layerName
    * @param prefix
    * @param imageFormat
    * @param srs
    * @param user
    * @param password 
    */
    public StreamSource(String host, int port, String layerName, String prefix,
            String imageFormat, String srs,
            String user, String password) {
        this.m_host = host;
        this.m_port = port;
        this.m_layerName = layerName;
        this.m_prefix = prefix;
        this.m_imageFormat = imageFormat;
        this.m_srs = srs;
        this.m_user = user;
        this.m_password = password;
    }
    
    /**
     * Get the host of the source
     * @return 
     */
    public String getHost() {
        return this.m_host;
    }

    /**
     * Set the host of the source with the parameter
     * @param host 
     */
    public void setHost(String host) {
        this.m_host = host;
    }

    /**
     * Get the port of the source
     * @return 
     */
    public int getPort() {
        return m_port;
    }

    /**
     * Set the port of the source with the parameter
     * @param port 
     */
    public void setPort(int port) {
        this.m_port = port;
    }

    /**
     * Get the name of the layer of the source
     * @return 
     */
    public String getLayerName() {
        return this.m_layerName;
    }

    /**
     * Set the name of the layer of the source with the parameter
     * @param layerName 
     */
    public void setLayerName(String layerName) {
        this.m_layerName = layerName;
    }

    /**
     * Get the format of the image 
     * @return 
     */
    public String getImageFormat() {
        return this.m_imageFormat;
    }

    /**
     * Set the format of the image with the parameter
     * @param imageFormat 
     */
    public void setImageFormat(String imageFormat) {
        this.m_imageFormat = imageFormat;
    }

    /**
     * Get the user of the source
     * @return 
     */
    public String getUser() {
        return m_user;
    }

    /**
     * Set the user of the source with the parameter
     * @param user 
     */
    public void setUser(String user) {
        this.m_user = user;
    }

    /**
     * Get the Password of the source
     * @return 
     */
    public String getPassword() {
        return m_password;
    }

    /**
     * Set the password of the source with the parameter
     * @param password 
     */
    public void setPassword(String password) {
        this.m_password = password;
    }

    /**
     * Set the srs of the source with the parameter
     * @param srs 
     */
    public void setSRS(String srs) {
        this.m_srs = srs;
    }

    /**
     * Get the srs of the source
     * @return 
     */
    public String getSRS() {
        return m_srs;
    }
    /**
     * Returns a human-readable description of this StreamSource
     *
     * @return a description String
     */
    @Override
    public String toString() {
        return m_host + ":" + m_port + "-" + m_layerName + "-" + m_user + "-" + m_password + "-" + m_imageFormat;
    }

    //TODO : give to this function a meaningful name
    public String getDbms() {
        return m_host + ":" + m_port + "//request=getMap&layers=" + m_layerName;
    }

    /**
     * Get the prefix of the source
     * @return 
     */
    public String getPrefix() {
        return m_prefix;
    }

    /**
     * Set the prefix of the source with the parameter
     * @param prefix 
     */
    public void setPrefix(String prefix) {
        this.m_prefix = prefix;
    }
}
