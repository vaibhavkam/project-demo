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

import com.invoice.service.ItemService;
import com.model.transaction.Item;
import com.model.util.ResponseCode;
import com.model.util.ResponseType;
import com.util.service.ResponseGenerator;

/**
 * @author vkamble
 * Controller to serve item CRUD requests
 */
@RestController
@RequestMapping("/item")
public class ItemController {

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);	

	@Autowired
	private ItemService itemService;
	
	/**
	 * Request end point to save item
	 * @param item
	 * @param bindingResult
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Object saveItem(@Valid @RequestBody Item item,BindingResult bindingResult){
		
		try{
			if (bindingResult.hasErrors()){
		        List<FieldError> errors = bindingResult.getFieldErrors();
		        List<String> causes = new ArrayList<String>();
		        for (FieldError e : errors){
		        	causes.add(e.getField().toUpperCase() + ":" + e.getDefaultMessage());
		        }
		        
				logger.debug("Validation error occurred while saving item");
		        return ResponseGenerator.createResponse(ResponseCode.VALIDATION_ERROR, ResponseCode.VALIDATION_ERROR.getMessage(), causes, null, ResponseType.ERROR);
			}
			
	        return ResponseGenerator.createResponse(ResponseCode.CREATE_SUCCESS, ResponseCode.CREATE_SUCCESS.getMessage(), ResponseCode.CREATE_SUCCESS.getMessage(), itemService.saveItem(item), ResponseType.ITEM);
			
		}catch(Exception e){
			logger.debug("Error occurred while saving item");
	        return ResponseGenerator.createResponse(ResponseCode.CREATE_ERROR, ResponseCode.CREATE_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);

		}
	}

	/**
	 * Request end point to update item details
	 * @param item
	 * @param bindingResult
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.PUT)
	public Object updateItem(@Valid @RequestBody Item item,BindingResult bindingResult){
		
		try{
			if (bindingResult.hasErrors()){
		        List<FieldError> errors = bindingResult.getFieldErrors();
		        List<String> causes = new ArrayList<String>();
		        for (FieldError e : errors){
		        	causes.add(e.getField().toUpperCase() + ":" + e.getDefaultMessage());
		        }
				logger.debug("Validation error occurred while updating item");
		        return ResponseGenerator.createResponse(ResponseCode.VALIDATION_ERROR, ResponseCode.VALIDATION_ERROR.getMessage(), causes, null, ResponseType.ERROR);
			}
			
			if(itemService.isItemExists(item.getId()))
		        return ResponseGenerator.createResponse(ResponseCode.UPDATE_SUCCESS, ResponseCode.UPDATE_SUCCESS.getMessage(), ResponseCode.UPDATE_SUCCESS.getMessage(), itemService.saveItem(item), ResponseType.ITEM);
			else{
				logger.debug("Item data not found");
		        return ResponseGenerator.createResponse(ResponseCode.DATA_NOT_FOUND_ERROR, ResponseCode.DATA_NOT_FOUND_ERROR.getMessage(), "Item with id="+item.getId()+" not found", null, ResponseType.ERROR);
			}
			
		}catch(Exception e){
			logger.debug("Error occurred while updating item");
	        return ResponseGenerator.createResponse(ResponseCode.UPDATE_ERROR, ResponseCode.UPDATE_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}
	
	/**
	 * Request end point to delete item
	 * @param id
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.DELETE,value = "/{id}")
	public Object deleteItem(@PathVariable("id") Long id){
		
		try{
			
			if(itemService.isItemExists(id)){
				itemService.deleteItem(id);
		        return ResponseGenerator.createResponse(ResponseCode.DELETE_SUCCESS, ResponseCode.DELETE_SUCCESS.getMessage(), "Item with id="+id+" is deleted", true, ResponseType.STATUS);

			}
			else{
				logger.debug("Item data not found");
		        return ResponseGenerator.createResponse(ResponseCode.DATA_NOT_FOUND_ERROR, ResponseCode.DATA_NOT_FOUND_ERROR.getMessage(), "Item with id="+id+" not found", null, ResponseType.ERROR);
			}			
		}
		catch(Exception e){
			logger.debug("Error occurred while deleting item");
	        return ResponseGenerator.createResponse(ResponseCode.DELETE_ERROR, ResponseCode.DELETE_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}
	
	/**
	 * Request end point to get item by id
	 * @param id
	 * @return object
	 */
	@RequestMapping(method=RequestMethod.GET,value = "/{id}")
	public Object getItem(@PathVariable("id") Long id){
		
		try{
			
			if(itemService.isItemExists(id))
		        return ResponseGenerator.createResponse(ResponseCode.READ_SUCCESS, ResponseCode.READ_SUCCESS.getMessage(), ResponseCode.READ_SUCCESS.getMessage(), itemService.getItem(id), ResponseType.ITEM);
			else{
				logger.debug("Item data not found");
		        return ResponseGenerator.createResponse(ResponseCode.DATA_NOT_FOUND_ERROR, ResponseCode.DATA_NOT_FOUND_ERROR.getMessage(), "Item with id="+id+" not found", null, ResponseType.ERROR);
			}			
		}
		catch(Exception e){
			logger.debug("Error occurred while reading item");
	        return ResponseGenerator.createResponse(ResponseCode.READ_ERROR, ResponseCode.READ_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
		
	}

	/**
	 * Request end point to get all items
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public Object getAllItem(){
		
		try{
			
	        return ResponseGenerator.createResponse(ResponseCode.READ_SUCCESS, ResponseCode.READ_SUCCESS.getMessage(), ResponseCode.READ_SUCCESS.getMessage(), itemService.getAllItems(), ResponseType.ITEM);
		}
		catch(Exception e){
			logger.debug("Error occurred while fetching items");
	        return ResponseGenerator.createResponse(ResponseCode.READ_ERROR, ResponseCode.READ_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}
	
	/**
	 * Request end point to get all items by invoiceId
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,path="/invoice")
	public Object getAllItemsForInvoiceId(@RequestParam("id") Long id){
		
		try{
			
	        return ResponseGenerator.createResponse(ResponseCode.READ_SUCCESS, ResponseCode.READ_SUCCESS.getMessage(), ResponseCode.READ_SUCCESS.getMessage(), itemService.getItemsByInvoiceId(id), ResponseType.ITEM);
		}
		catch(Exception e){
			logger.debug("Error occurred while fetching items");
	        return ResponseGenerator.createResponse(ResponseCode.READ_ERROR, ResponseCode.READ_ERROR.getMessage(), e.getMessage(), null, ResponseType.ERROR);
		}
	}
}
