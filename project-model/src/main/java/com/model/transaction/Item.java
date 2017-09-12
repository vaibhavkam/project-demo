/**
 * 
 */
package com.model.transaction;

/**
 * @author vkamble
 *
 */
public class Item extends Transaction{

	private String description;
		
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
