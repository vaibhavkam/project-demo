/**
 * 
 */
package com.invoice.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.invoice.service.InvoiceService;
import com.model.transaction.Invoice;
import com.model.util.ResponseCode;
import com.model.util.ResponseType;
import com.util.service.ResponseGenerator;

/**
 * @author vkamble
 * Controller to serve invoice CRUD requests
 */
@RestController
@RequestMapping("/invoice")
public class InvoiceController {

	private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);	

	@Autowired
	InvoiceService invoiceService;
	
	/**
	 * Request end point to save invoice
	 * @param invoice
	 * @param bindingResult
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Object saveInvoice(@Valid @RequestBody Invoice invoice,BindingResult bindingResult){
		
		try{
			if (bindingResult.hasErrors()){
		        List<FieldError> errors = bindingResult.getFieldErrors();
		        List<String> causes = new ArrayList<String>();
		        for (FieldError e : errors){
		        	causes.add(e.getField().toUpperCase() + ":" + e.getDefaultMessage());
		        }
		        
				logger.debug("Validation error occurred while saving invoice");
		        return ResponseGenerator.createResponse(ResponseCode.VALIDATION_ERROR, ResponseCode.VALIDATION_ERROR.getMessage(), causes, null, ResponseType.ERROR);
			}
			
	        return ResponseGenerator.createResponse(ResponseCode.CREATE_SUCCESS, ResponseCode.CREATE_SUCCESS.getMessage(), ResponseCode.CREATE_SUCCESS.getMessage(), invoiceService.saveInvoice(invoice), ResponseType.INVOICE);
			
		}catch(Exception e){
			logger.debug("Error occurred while saving invoice");
	        return ResponseGenerator.createResponse(ResponseCode.CREATE_ERROR, ResponseCode.CREATE_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);

		}
	}


	/**
	 * Request end point to update invoice details
	 * @param invoice
	 * @param bindingResult
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public Object updateInvoice(@Valid @RequestBody Invoice invoice,BindingResult bindingResult){
		
		try{
			if (bindingResult.hasErrors()){
		        List<FieldError> errors = bindingResult.getFieldErrors();
		        List<String> causes = new ArrayList<String>();
		        for (FieldError e : errors){
		        	causes.add(e.getField().toUpperCase() + ":" + e.getDefaultMessage());
		        }
				logger.debug("Validation error occurred while updating invoice");
		        return ResponseGenerator.createResponse(ResponseCode.VALIDATION_ERROR, ResponseCode.VALIDATION_ERROR.getMessage(), causes, null, ResponseType.ERROR);
			}
			
			if(invoiceService.isInvoiceExists(invoice.getId())){
				
		        return ResponseGenerator.createResponse(ResponseCode.UPDATE_SUCCESS, ResponseCode.UPDATE_SUCCESS.getMessage(), ResponseCode.UPDATE_SUCCESS.getMessage(), invoiceService.saveInvoice(invoice), ResponseType.INVOICE);
			}
			else{
				logger.debug("Invoice data not found");
		        return ResponseGenerator.createResponse(ResponseCode.DATA_NOT_FOUND_ERROR, ResponseCode.DATA_NOT_FOUND_ERROR.getMessage(), "Invoice with id="+invoice.getId()+" not found", null, ResponseType.ERROR);
			}
			
		}catch(Exception e){
			logger.debug("Error occurred while updating invoice");
	        return ResponseGenerator.createResponse(ResponseCode.UPDATE_ERROR, ResponseCode.UPDATE_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}
	
	/**
	 * Request end point to delete invoice
	 * @param id
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.DELETE,value = "/{id}")
	public Object deleteInvoice(@PathVariable("id") Long id){
		
		try{
			
			if(invoiceService.isInvoiceExists(id)){
				invoiceService.deleteInvoice(id);
		        return ResponseGenerator.createResponse(ResponseCode.DELETE_SUCCESS, ResponseCode.DELETE_SUCCESS.getMessage(), "Invoice with id="+id+" is deleted",true,ResponseType.STATUS);

			}
			else{
				logger.debug("Invoice data not found");
		        return ResponseGenerator.createResponse(ResponseCode.DATA_NOT_FOUND_ERROR, ResponseCode.DATA_NOT_FOUND_ERROR.getMessage(), "Invoice with id="+id+" not found", null, ResponseType.ERROR);
			}			
		}
		catch(Exception e){
			logger.debug("Error occurred while deleting invoice");
	        return ResponseGenerator.createResponse(ResponseCode.DELETE_ERROR, ResponseCode.DELETE_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}
	
	/**
	 * Request end point to get invoice by id
	 * @param id
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.GET,value = "/{id}")
	public Object getInvoice(@PathVariable("id") Long id){
		
		try{
			
			if(invoiceService.isInvoiceExists(id))
		        return ResponseGenerator.createResponse(ResponseCode.READ_SUCCESS, ResponseCode.READ_SUCCESS.getMessage(), ResponseCode.READ_SUCCESS.getMessage(), invoiceService.getInvoice(id), ResponseType.INVOICE);
			else{
				logger.debug("Invoice data not found");
		        return ResponseGenerator.createResponse(ResponseCode.DATA_NOT_FOUND_ERROR, ResponseCode.DATA_NOT_FOUND_ERROR.getMessage(), "Invoice with id="+id+" not found", null, ResponseType.ERROR);
			}			
		}
		catch(Exception e){
			logger.debug("Error occurred while reading invoice");
	        return ResponseGenerator.createResponse(ResponseCode.READ_ERROR, ResponseCode.READ_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}

	/**
	 * Request end point to get all invoices
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public Object getAllInvoice(){
		
		try{
	        return ResponseGenerator.createResponse(ResponseCode.READ_SUCCESS, ResponseCode.READ_SUCCESS.getMessage(), ResponseCode.READ_SUCCESS.getMessage(), invoiceService.getAllInvoices(), ResponseType.INVOICE);
		}
		catch(Exception e){
			logger.debug("Error occurred while fetching invoices");
	        return ResponseGenerator.createResponse(ResponseCode.READ_ERROR, ResponseCode.READ_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}	
	
	/**
	 * Request end point to get all invoices by customerId
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,path="/customer")
	public Object getAllInvoiceForCustomerId(@RequestParam("id") Long id){
		
		try{
			
	        return ResponseGenerator.createResponse(ResponseCode.READ_SUCCESS, ResponseCode.READ_SUCCESS.getMessage(), ResponseCode.READ_SUCCESS.getMessage(), invoiceService.getAllInvoicesForCustomerId(id), ResponseType.INVOICE);
		}
		catch(Exception e){
			logger.debug("Error occurred while fetching invoices");
	        return ResponseGenerator.createResponse(ResponseCode.READ_ERROR, ResponseCode.READ_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}
}
