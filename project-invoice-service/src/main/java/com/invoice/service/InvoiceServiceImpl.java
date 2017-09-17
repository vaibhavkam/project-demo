/**
 * 
 */
package com.invoice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.entity.InvoiceEntity;
import com.invoice.entity.ItemEntity;
import com.invoice.repository.InvoiceRepository;
import com.model.transaction.Invoice;
import com.model.transaction.Item;
import com.model.user.Customer;

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
	
	/**
	 * Method to save invoice
	 * @param invoice
	 * @return Invoice
	 * @throws Exception 
	 */
	@Override
	public Invoice saveInvoice(Invoice invoice) throws Exception {
		
		if(invoice.getItems()==null || invoice.getItems().size()==0)
			throw new Exception("Invoice should contains atleast one item");
						
		InvoiceEntity invoiceEntity = getInvoiceEntityFromInvoiceModel(invoice);
		invoiceRepository.save(invoiceEntity);
		invoice=getInvoice(invoiceEntity.getId());
		return invoice;
	}
	
	/**
	 * Method to update invoice
	 * @param invoice
	 * @return Invoice
	 * @throws Exception 
	 */
	@Override
	public Invoice updateInvoice(Invoice invoice) throws Exception {
		
		InvoiceEntity invoiceEntity = getInvoiceEntityFromInvoiceModel(invoice);
		invoiceRepository.save(invoiceEntity);
		invoice=getInvoice(invoiceEntity.getId());
		return invoice;
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
	public boolean deleteInvoice(Long id) {
		invoiceRepository.delete(id);
		return true;
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
		
		return invoice;
	}
}
