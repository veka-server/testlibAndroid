package vekaUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class PostRequest {

	private String url, responseString;
	private Hashtable<String,String> datas = new Hashtable<String,String>();

	public PostRequest(String url){
		setUrl(url);		
	}
	
	/**
	 * Set the url for the HTTP request
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Get the url for the HTTP request
	 * return String url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Associate the specified value with the specified key in this Params. If the key already exists, the old value is replaced. The key and value cannot be null.
	 * @param key String
	 * @param value String
	 */
	public void putParam(String key, String value ) {
		datas.put(key,value);
	}

	/**
	 * Get the value associated to the key.
	 * @param key
	 * @return value
	 */
	public String getParam(String key) {
		return datas.get(key);
	}
	
	/**
	 * Get the Hashtable<String,String> with contain all param for the request
	 * @return Hashtable<String,String>
	 */
	public Hashtable<String,String> getAllParams() {
		return datas;
	}
	
	/**
	 * Removes the key/value pair with the specified key from this Params.
	 * @param key
	 */
	public void removeParam(String key) {
		datas.remove(key);
	}
		
	/**
	 * Send the request
	 * @return HttpResponse
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse postData() throws ClientProtocolException, IOException {
	    // Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);

        Set<String> set = datas.keySet();
        Iterator<String> itr = set.iterator();
        
        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(datas.size());

    	// ajout des parametre
        while (itr.hasNext()) {
		  String key = itr.next();
		  String value = datas.get(key);
		  nameValuePairs.add(new BasicNameValuePair(key, value));	
        }

        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);

		HttpEntity entity = response.getEntity();
		responseString = EntityUtils.toString(entity, "UTF-8");
		
        return response;        
	} 
	
	/**
	 * Get the Response of the server
	 * @return String
	 */
	public String getResponse(){
		return responseString;
	}
	

}
