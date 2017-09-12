/**
 * 
 */
package com.util.service;

import java.util.HashMap;
import java.util.Map;

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
	    
	    if(responseType==ResponseType.CUSTOMER)
	    	uri+="/customerDemo/customer/{id}";
	    else if(responseType==ResponseType.INVOICE)
	    	uri+="/invoiceDemo/invoice/{id}";
	    else
	    	uri+="/invoiceDemo/item/{id}";
	   
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("id", id.toString());
	     
	    RestTemplate restTemplate = new RestTemplate();
	    Response response = restTemplate.getForObject(uri, Response.class, params);
	     
	    return response;
	}
}
