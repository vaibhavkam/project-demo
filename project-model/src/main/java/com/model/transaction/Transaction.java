/**
 * 
 */
package com.model.transaction;

import javax.validation.constraints.NotNull;

import com.model.meta.Entity;

/**
 * @author vkamble
 *
 */
public class Transaction extends Entity{

	@NotNull
	private double amount;

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
