/**
 * 
 */
package com.model.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vkamble
 *
 */
public class Response {

	private ResponseCode responseCode;
	private String message;
	private List<String> causes;
	private Object object;
	private ResponseType responseType;
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the cause
	 */
	public List<String> getCauses() {
		return causes;
	}
	/**
	 * @param cause the cause to set
	 */
	public void addCause(String cause) {
		if(causes==null)
			causes=new ArrayList<String>();
		this.causes.add(cause);
	}
	public ResponseCode getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public ResponseType getResponseType() {
		return responseType;
	}
	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}
}
