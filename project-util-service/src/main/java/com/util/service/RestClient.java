/**
 * 
 */
package com.util.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import com.model.util.Response;
import com.model.util.ResponseType;

/**
 * @author vkamble
 *
 */
public class RestClient {

	public static Response getResourceById(Long id,ResponseType responseType)
	{
		
	    String uri = "http://localhost:8080";
	    String userName=null;
	    String password=null;

	    if(responseType==ResponseType.CUSTOMER){
	    	uri+="/customerDemo/customer/{id}";
	    	userName="customerServiceUser";
	    	password="password";
	    }
	    else if(responseType==ResponseType.INVOICE){
	    	uri+="/invoiceDemo/invoice/{id}";
	    	userName="invoiceServiceUser";
	    	password="password";
	    }
	    else{
	    	uri+="/invoiceDemo/item/{id}";
	    	userName="invoiceServiceUser";
	    	password="password";
	    }
	   
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("id", id.toString());
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(userName, password));	    
	    Response response = restTemplate.getForObject(uri, Response.class, params);
	     
	    return response;
	}
}
