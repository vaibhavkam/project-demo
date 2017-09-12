/**
 * 
 */
package com.util.service;

import java.util.List;

import com.model.util.Response;
import com.model.util.ResponseCode;
import com.model.util.ResponseType;

/**
 * @author vkamble
 *
 */
public class ResponseGenerator {

	public static Response createResponse(ResponseCode responseCode, String message, String cause, Object object, ResponseType responseType){

        Response response = new Response();
        response.setResponseCode(responseCode);
        response.setMessage(message);
        response.addCause(cause);
        response.setObject(object);
        response.setResponseType(responseType);
        return response;		
	}
	
	public static Response createResponse(ResponseCode responseCode, String message, List<String> causes, Object object, ResponseType responseType){

        Response response = new Response();
        response.setResponseCode(responseCode);
        response.setMessage(message);
        response.setObject(object);
        response.setResponseType(responseType);
        for(String cause:causes)
        	response.addCause(cause);
        return response;		
	}
}
