/**
 * 
 */
package com.model.user;

import javax.validation.constraints.NotNull;

import com.model.meta.Entity;

/**
 * @author vkamble
 *
 */
public class Person extends Entity{

	@NotNull
	private String name;
	private String emailId;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}
	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
}
