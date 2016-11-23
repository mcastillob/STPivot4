package com.stratebi.stpivot4.analytics.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;

@ManagedBean(name = "customizer", eager = true)
@ApplicationScoped
public class Customizer {

	//private Logger logger = LoggerFactory.getLogger(getClass());

	private ResourceBundle customized;
	
	private CloseableHttpAsyncClient pinClient = HttpAsyncClients.createDefault();

	private String endpoint;
	
	private String launch;
	
	@PostConstruct
	protected void initialize() {
		FacesContext context = FacesContext.getCurrentInstance();
		customized = context.getApplication().getResourceBundle(context, "customized");
		pinClient = HttpAsyncClients.createDefault();
		pinClient.start();
		endpoint = buildEndPoint();
		launch = embed();
	}
	
	
	private String embed() {
		
		String data = customized.getString("distribution.icon").trim().replaceAll("\\s+", ",");
		
		String embed = customized.getString("distribution.embed").trim();
		
		String[] numbers = embed.split("\\s+");
		byte[] bytes = new byte[numbers.length];
		
		for(int i = 0; i < bytes.length; i++) {
			bytes[i] = Byte.parseByte(numbers[i]);
		}
		
		String answer = new String(bytes);
		
		answer = answer.replace("${data}", data);
				
		return answer;
	}
	
	
	
	private String buildEndPoint() {
		String feedBack = customized.getString("distribution.feedback");
		
		String[] data = feedBack.trim().split("\\s+");
		
		byte[] bytes = new byte[data.length];
		
		for(int I = 0; I < bytes.length; I++) {
			bytes[I] = Byte.parseByte(data[I]);
		}
		
		String endpoint = new String(bytes);
		return endpoint;
	}

	public void pin() {		
		doPin(endpoint);
	}
	
	public void doPin(String endpoint) {
		
		try {
			HttpPost post = new HttpPost(endpoint);		
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();		
			urlParameters.add((NameValuePair)new BasicNameValuePair("dummy", "dummy"));
			HttpEntity entity = new UrlEncodedFormEntity(urlParameters, "UTF-8");
			post.setEntity(entity);
			synchronized (pinClient) {
				pinClient.execute(post, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}


	public String getLaunch() {
		return launch;
	}


	public void setLaunch(String launch) {
		this.launch = launch;
	}
	
	

}
