/**
 * 
 */
package com.customer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.entity.CustomerEntity;
import com.customer.repository.CustomerRepository;
import com.model.user.Customer;

/**
 * @author vkamble
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	CustomerRepository customerRepository;
		
	/**
	 * Method to save customer
	 * @param customer
	 * @return Customer
	 */
	@Override
	public Customer saveCustomer(Customer customer) {
		CustomerEntity customerEntity = getCustomerEntityFromCustomerModel(customer);
		customerRepository.save(customerEntity);
		customer=getCustomerModelFromCustomerEntity(customerEntity);
		return customer;
	}
	
	/**
	 * Method to get customer by Id
	 * @param id
	 * @return Customer
	 */
	@Override
	public Customer getCustomer(Long id) {
		
		CustomerEntity customerEntity = customerRepository.findOne(id);
		if(customerEntity!=null)
			return getCustomerModelFromCustomerEntity(customerEntity);
		return null;
	}
	
	/**
	 * Method to check if customer exists or not
	 * @param id
	 * @return boolean
	 */
	@Override
	public boolean isCustomerExists(Long id) {
		return customerRepository.exists(id);
	}

	/**
	 * Method to delete customer
	 * @param id
	 */
	@Override
	public boolean deleteCustomer(Long id) {
		customerRepository.delete(id);
		return true;
	}

	/**
	 * Method to get all customers
	 * @return List<Customer>
	 */
	@Override
	public List<Customer> getAllCustomers() {
		Iterable<CustomerEntity> customerEntities = customerRepository.findAll();
		List<Customer> customers = new ArrayList<>();
		if(customerEntities!=null){
			for(CustomerEntity customerEntity:customerEntities){
				customers.add(getCustomerModelFromCustomerEntity(customerEntity));
			}
		}
		return customers;
	}

	/**
	 * Helper function to convert Customer model to Customer Entity
	 * @param customer
	 * @return CustomerEntity
	 */
	public CustomerEntity getCustomerEntityFromCustomerModel(Customer customer){
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setId(customer.getId());
		customerEntity.setName(customer.getName());
		customerEntity.setEmailId(customer.getEmailId());
		customerEntity.setCreatedDate(customer.getCreatedDate());
		customerEntity.setUpdatedDate(customer.getUpdatedDate());
		return customerEntity;
	}
	
	/**
	 * Helper function to convert Customer entity to Customer model
	 * @param customerEntity
	 * @return Customer
	 */
	public Customer getCustomerModelFromCustomerEntity(CustomerEntity customerEntity){
		Customer customer = new Customer();
		customer.setId(customerEntity.getId());
		customer.setName(customerEntity.getName());
		customer.setEmailId(customerEntity.getEmailId());
		customer.setCreatedDate(customerEntity.getCreatedDate());
		customer.setUpdatedDate(customerEntity.getUpdatedDate());
		return customer;
	}

}
