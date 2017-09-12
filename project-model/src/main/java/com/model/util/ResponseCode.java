/**
 * 
 */
package com.model.util;

/**
 * @author vkamble
 *
 */
public enum ResponseCode {

	CREATE_ERROR("Error while creating data"),
	UPDATE_ERROR("Error while updating data"),
	DELETE_ERROR("Error while deleting data"),
	READ_ERROR("Error while reading data"),
	DATA_NOT_FOUND_ERROR("Requested data not found"),
	VALIDATION_ERROR("Data is invalid"),
	SERVICE_NOT_AVAILABLE_ERROR("Service is not available"),
	CREATE_SUCCESS("Data sucessfully created"),
	UPDATE_SUCCESS("Data sucessfully updated"),
	READ_SUCCESS("Data sucessfully fetched"),
	DELETE_SUCCESS("Data sucessfully deleted"),
	BAD_REQUEST("Request body/paramters are missing");

	
	private String message;

	private ResponseCode(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
}
