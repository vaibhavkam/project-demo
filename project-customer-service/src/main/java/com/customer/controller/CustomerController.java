/**
 * 
 */
package com.customer.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.customer.service.CustomerService;
import com.model.user.Customer;
import com.model.util.ResponseCode;
import com.model.util.ResponseType;
import com.util.service.ResponseGenerator;

/**
 * @author vkamble
 * Controller to serve customer CRUD requests
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);	

	@Autowired
	private CustomerService customerService;
	
	/**
	 * Request end point to save customer
	 * @param customer
	 * @param bindingResult
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Object saveCustomer(@Valid @RequestBody Customer customer,BindingResult bindingResult){
		
		try{
			if (bindingResult.hasErrors()){
		        List<FieldError> errors = bindingResult.getFieldErrors();
		        List<String> causes = new ArrayList<String>();
		        for (FieldError e : errors){
		        	causes.add(e.getField().toUpperCase() + ":" + e.getDefaultMessage());
		        }
		        
				logger.debug("Validation error occurred while saving customer");
		        return ResponseGenerator.createResponse(ResponseCode.VALIDATION_ERROR, ResponseCode.VALIDATION_ERROR.getMessage(), causes, null, ResponseType.ERROR);
			}
			
	        return ResponseGenerator.createResponse(ResponseCode.CREATE_SUCCESS, ResponseCode.CREATE_SUCCESS.getMessage(), ResponseCode.CREATE_SUCCESS.getMessage(), customerService.saveCustomer(customer), ResponseType.CUSTOMER);

			
		}catch(Exception e){
			logger.debug("Error occurred while saving customer");
	        return ResponseGenerator.createResponse(ResponseCode.CREATE_ERROR, ResponseCode.CREATE_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);

		}
	}

	/**
	 * Request end point to update customer details
	 * @param customer
	 * @param bindingResult
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public Object updateCustomer(@Valid @RequestBody Customer customer,BindingResult bindingResult){
		
		try{
			if (bindingResult.hasErrors()){
		        List<FieldError> errors = bindingResult.getFieldErrors();
		        List<String> causes = new ArrayList<String>();
		        for (FieldError e : errors){
		        	causes.add(e.getField().toUpperCase() + ":" + e.getDefaultMessage());
		        }
				logger.debug("Validation error occurred while updating customer");
		        return ResponseGenerator.createResponse(ResponseCode.VALIDATION_ERROR, ResponseCode.VALIDATION_ERROR.getMessage(), causes, null, ResponseType.ERROR);
			}
			
			if(customerService.isCustomerExists(customer.getId()))
	        	return ResponseGenerator.createResponse(ResponseCode.UPDATE_SUCCESS, ResponseCode.UPDATE_SUCCESS.getMessage(), ResponseCode.UPDATE_SUCCESS.getMessage(), customerService.saveCustomer(customer), ResponseType.CUSTOMER);

			else{
				logger.debug("Customer data not found");
		        return ResponseGenerator.createResponse(ResponseCode.DATA_NOT_FOUND_ERROR, ResponseCode.DATA_NOT_FOUND_ERROR.getMessage(), "Customer with id="+customer.getId()+" not found", null, ResponseType.ERROR);
			}
			
		}catch(Exception e){
			logger.debug("Error occurred while updating customer");
	        return ResponseGenerator.createResponse(ResponseCode.UPDATE_ERROR, ResponseCode.UPDATE_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}
	
	/**
	 * Request end point to delete customer
	 * @param id
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.DELETE,value = "/{id}")
	public Object deleteCustomer(@PathVariable("id") Long id){
		
		try{
			
			if(customerService.isCustomerExists(id)){
				customerService.deleteCustomer(id);
		        return ResponseGenerator.createResponse(ResponseCode.DELETE_SUCCESS, ResponseCode.DELETE_SUCCESS.getMessage(), "Customer with id="+id+" is deleted", null, ResponseType.STATUS);

			}
			else{
				logger.debug("Customer data not found");
		        return ResponseGenerator.createResponse(ResponseCode.DATA_NOT_FOUND_ERROR, ResponseCode.DATA_NOT_FOUND_ERROR.getMessage(), "Customer with id="+id+" not found", null, ResponseType.ERROR);
			}			
		}
		catch(Exception e){
			logger.debug("Error occurred while deleting customer");
	        return ResponseGenerator.createResponse(ResponseCode.DELETE_ERROR, ResponseCode.DELETE_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}
	
	/**
	 * Request end point to get customer by id
	 * @param id
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.GET,value = "/{id}")
	public Object getCustomer(@PathVariable("id") Long id){
		
		try{
			
			if(customerService.isCustomerExists(id))
	        	return ResponseGenerator.createResponse(ResponseCode.READ_SUCCESS, ResponseCode.READ_SUCCESS.getMessage(), ResponseCode.READ_SUCCESS.getMessage(), customerService.getCustomer(id), ResponseType.CUSTOMER);
			else{
				logger.debug("Customer data not found");
		        return ResponseGenerator.createResponse(ResponseCode.DATA_NOT_FOUND_ERROR, ResponseCode.DATA_NOT_FOUND_ERROR.getMessage(), "Customer with id="+id+" not found", null, ResponseType.ERROR);
			}			
		}
		catch(Exception e){
			logger.debug("Error occurred while reading customer");
	        return ResponseGenerator.createResponse(ResponseCode.READ_ERROR, ResponseCode.READ_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
		
	}

	/**
	 * Request end point to get all customers
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public Object getAllCustomer(){
		
		try{
        	return ResponseGenerator.createResponse(ResponseCode.READ_SUCCESS, ResponseCode.READ_SUCCESS.getMessage(), ResponseCode.READ_SUCCESS.getMessage(), customerService.getAllCustomers(), ResponseType.CUSTOMER);
		}
		catch(Exception e){
			logger.debug("Error occurred while fetching customers");
	        return ResponseGenerator.createResponse(ResponseCode.READ_ERROR, ResponseCode.READ_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}
}
