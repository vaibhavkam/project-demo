/**
 * 
 */
package com.invoice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.entity.InvoiceEntity;
import com.invoice.entity.ItemEntity;
import com.invoice.repository.InvoiceRepository;
import com.model.transaction.Invoice;
import com.model.transaction.Item;
import com.model.user.Customer;
import com.model.util.Response;
import com.model.util.ResponseType;
import com.util.service.RestClient;

/**
 * @author vkamble
 *
 */
@Service
public class InvoiceServiceImpl implements InvoiceService{
	
	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Autowired
	ItemService itemService;
	
    @Autowired
    ObjectMapper objectMapper;

	/**
	 * Method to save invoice
	 * @param invoice
	 * @return Invoice
	 * @throws Exception 
	 */
	@Override
	public Invoice saveInvoice(Invoice invoice) throws Exception {
		Response response = RestClient.getResourceById(invoice.getCustomer().getId(), ResponseType.CUSTOMER);
		if(response.getResponseType()==ResponseType.CUSTOMER){
			Customer customer = objectMapper.convertValue(response.getObject(), Customer.class);
			if(customer.getId()==invoice.getCustomer().getId()){
				InvoiceEntity invoiceEntity = getInvoiceEntityFromInvoiceModel(invoice);
				invoiceRepository.save(invoiceEntity);
				invoice=getInvoice(invoiceEntity.getId());
				return invoice;
			}else
				throw new Exception("Invalid customer attached to invoice");
		}else
			throw new Exception("Invalid response from customer service");
	}
	
	/**
	 * Method to get invoice by Id
	 * @param id
	 * @return Invoice
	 */
	@Override
	public Invoice getInvoice(Long id) {
		
		InvoiceEntity invoiceEntity = invoiceRepository.findOne(id);
		if(invoiceEntity!=null)
			return getInvoiceModelFromInvoiceEntity(invoiceEntity);
		return null;
	}

	/**
	 * Method to check if invoice exists or not
	 * @param id
	 * @return boolean
	 */
	@Override
	public boolean isInvoiceExists(Long id) {
		return invoiceRepository.exists(id);
	}
	
	/**
	 * Method to delete invoice
	 * @param id
	 */
	@Override
	public void deleteInvoice(Long id) {
		invoiceRepository.delete(id);
	}


	/**
	 * Method to get all invoices
	 * @return List<Invoice>
	 */
	@Override
	public List<Invoice> getAllInvoices() {
		Iterable<InvoiceEntity> invoiceEntities = invoiceRepository.findAll();
		List<Invoice> invoices = new ArrayList<Invoice>();
		if(invoiceEntities!=null){
			for(InvoiceEntity invoiceEntity:invoiceEntities){
				invoices.add(getInvoiceModelFromInvoiceEntity(invoiceEntity));
			}
		}
		return invoices;
	}

	/**
	 * Method to get all invoices by customerId
	 * @return List<Invoice>
	 */
	@Override
	public List<Invoice> getAllInvoicesForCustomerId(Long customerId) {
		Iterable<InvoiceEntity> invoiceEntities = invoiceRepository.findByCustomerId(customerId);
		List<Invoice> invoices = new ArrayList<Invoice>();
		if(invoiceEntities!=null){
			for(InvoiceEntity invoiceEntity:invoiceEntities){
				invoices.add(getInvoiceModelFromInvoiceEntity(invoiceEntity));
			}
		}
		return invoices;
	}


	/**
	 * Helper function to convert Invoice model to Invoice Entity
	 * @param invoice
	 * @return InvoiceEntity
	 */
	public InvoiceEntity getInvoiceEntityFromInvoiceModel(Invoice invoice){
		InvoiceEntity invoiceEntity = new InvoiceEntity();
		invoiceEntity.setId(invoice.getId());
		invoiceEntity.setCustomerId(invoice.getCustomer().getId());
		invoiceEntity.setCreatedDate(invoice.getCreatedDate());
		invoiceEntity.setUpdatedDate(invoice.getUpdatedDate());

		if(invoice.getItems()!=null){
			for(Item item:invoice.getItems()){
				ItemEntity itemEntity =itemService.getItemEntityFromItemModel(item);
				invoiceEntity.addItem(itemEntity);
			}
		}
		return invoiceEntity;
	}
	
	/**
	 * Helper function to convert Invoice entity to Invoice model
	 * @param invoiceEntity
	 * @return Invoice
	 */
	public Invoice getInvoiceModelFromInvoiceEntity(InvoiceEntity invoiceEntity){
		long invoiceAmount=0;
		Invoice invoice = new Invoice();
		invoice.setId(invoiceEntity.getId());
		Customer customer = new Customer();
		customer.setId(invoiceEntity.getCustomerId());
		invoice.setCustomer(customer);
		invoice.setCreatedDate(invoiceEntity.getCreatedDate());
		invoice.setUpdatedDate(invoiceEntity.getUpdatedDate());
		if(invoiceEntity.getItems()!=null){
			for(ItemEntity itemEntity:invoiceEntity.getItems()){
				Item item =itemService.getItemModelFromItemEntity(itemEntity);
				invoice.addItem(item);
				invoiceAmount+=itemEntity.getAmount();
			}
		}
		invoice.setAmount(invoiceAmount);
		
		Response response = RestClient.getResourceById(invoice.getCustomer().getId(), ResponseType.CUSTOMER);
		if(response.getResponseType()==ResponseType.CUSTOMER){
			Customer customerResult = objectMapper.convertValue(response.getObject(), Customer.class);
			if(customerResult.getId()==invoice.getCustomer().getId()){
				invoice.setCustomer(customerResult);
			}
		}
		return invoice;
	}
}
