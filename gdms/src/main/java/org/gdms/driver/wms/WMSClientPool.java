package org.gdms.driver.wms;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;

import org.gvsig.remoteClient.wms.WMSClient;

/**
 * Pool for all the WMSClient. We can manage easily all the WMSClient
 * 
 */
public class WMSClientPool {

	private static HashMap<String, WMSClient> clients = new HashMap<String, WMSClient>();

        /**
         * Get back the WMSClient.
         * If we never create a connection to the remote server we initialize a new client.
         * the second parameter is at true : getCapabilities(null, true, null)
         * In the other case we return the old WMSClient
         * 
         * @param host
         * @return
         * @throws ConnectException
         * @throws IOException 
         */
	public static WMSClient getWMSClient(String host) throws ConnectException,
			IOException {
		WMSClient client = clients.get(host);
		if (client == null) {
			client = new WMSClient(host);
			client.getCapabilities(null, true, null);
			clients.put(host, client);
			return client;
		}
		return client;
	}
}
