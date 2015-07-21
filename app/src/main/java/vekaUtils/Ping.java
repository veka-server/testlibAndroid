package vekaUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import javax.net.ssl.SSLException;

/**
 * Class for ping a HTTP URL
 * @author veka
 *
 */
abstract public class Ping extends Thread {

	  private String url;
	  private int port = 0;
	  private int timeout = 3000;
	  	  
	  /**
	   * Create an instance of Ping class for a HTTP ping
	   * @param url
	   */
	  public Ping(String url) {
		    this.url = url;
	  }
	  
	  /**
	   * Create an instance of Ping class for a classic ping
	   * @param url
	   * @param port
	   */
	  public Ping(String url, int port) {
		    this.url = url;
		    this.port = port;
	  }

	  /**
	 * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in 
	 * the 200-399 range.
	 * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
	 * given timeout, otherwise <code>false</code>.
	 * @throws SSLException 
	 * @throws UnknownHostException 
	 */
	private boolean pingHttp() throws SSLException, UnknownHostException {
		boolean flag = false;
		 HttpURLConnection connection;
	    try {
	        connection = (HttpURLConnection) new URL(url).openConnection();	
		} catch (IOException e) {
	        return false;
	    }
	
	    connection.setConnectTimeout(timeout);
	    connection.setReadTimeout(timeout);
		    
	    try {
	        int responseCode = connection.getResponseCode();
	    	flag = ((200 >= responseCode) && (responseCode < 400));
	
		} catch (MalformedURLException e) {
	        flag = false;
		} catch (SSLException e) {
			throw e;
		} catch (UnknownHostException e) {
			throw e;
		} catch (IOException e) {
			flag = false;
	    } finally {
		      connection.disconnect();
	    }
    
        return flag;


	
	}

	private boolean ping() throws UnknownHostException {

		Socket socket = null;
		boolean reachable = false;
		try {
		    socket = new Socket(url, port);
		    reachable = true;
		} catch (UnknownHostException e) {
			throw e;
		} catch (IOException e) {
		} finally {            
		    if (socket != null) try { socket.close(); } catch(IOException e) {}
		}
		return reachable;
	}
	
	final public void run() {
		
		boolean b = false;
		
		try{
			if(port == 0){
				b = pingHttp();
			}
			else{
				b = ping();
			}

			if(b)
				sucess();
			else
				fail();		
		} catch (SSLException e) {
			warningSSL();
		} catch (UnknownHostException e) {
			failUnknownHost();
		}

	  }
	  
	/**
	   * Method call if ping fail with Unknown host error
	 */
	  public abstract void failUnknownHost();

	/**
	   * Method call if ping sucess
	   */
	  public abstract void sucess();
	  
	  /**
	   * Method call if ping fail
	   */
	  public abstract void fail();

	  /**
	   * Method call if ping fail with SSL error
	   */
	  public abstract void warningSSL();

}
