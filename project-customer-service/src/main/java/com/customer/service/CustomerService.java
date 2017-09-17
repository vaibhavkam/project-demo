/**
 * 
 */
package com.customer.service;

import java.util.List;

import com.customer.entity.CustomerEntity;
import com.model.user.Customer;

/**
 * @author vkamble
 *
 */
public interface CustomerService {

	/**
	 * Method to save customer
	 * @param customer
	 * @return Customer
	 */
	public Customer saveCustomer(Customer customer);
	
	/**
	 * Method to get customer by Id
	 * @param id
	 * @return Customer
	 */
	public Customer getCustomer(Long id);
	
	/**
	 * Method to check if customer exists or not
	 * @param id
	 * @return boolean
	 */
	public boolean isCustomerExists(Long id);
	
	/**
	 * Method to delete customer
	 * @param id
	 * @return 
	 */
	public boolean deleteCustomer(Long id);
	
	/**
	 * Method to get all customers
	 * @return List<Customer>
	 */
	public List<Customer> getAllCustomers();
	
	/**
	 * Helper function to convert Customer model to Customer Entity
	 * @param customer
	 * @return CustomerEntity
	 */
	public CustomerEntity getCustomerEntityFromCustomerModel(Customer customer);
	
	/**
	 * Helper function to convert Customer entity to Customer model
	 * @param customerEntity
	 * @return Customer
	 */
	public Customer getCustomerModelFromCustomerEntity(CustomerEntity customerEntity);
}
