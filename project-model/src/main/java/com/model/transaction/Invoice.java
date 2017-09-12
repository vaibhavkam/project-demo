/**
 * 
 */
package com.model.transaction;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.model.user.Customer;

/**
 * @author vkamble
 *
 */
public class Invoice extends Transaction {

	@NotNull
	private Customer customer;
	private List<Item> items=new ArrayList<Item>();
	
	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}
	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	/**
	 * @return the items
	 */
	public List<Item> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public void addItem(Item item) {
		if(items==null)
			items = new ArrayList<Item>();
		items.add(item);
	}
	public boolean deleteItem(Item item) {
		if(items==null || !items.contains(item))
			return false;
		return items.remove(item);
	}
}
