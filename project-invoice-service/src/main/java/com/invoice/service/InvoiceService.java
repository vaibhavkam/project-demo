/**
 * 
 */
package com.invoice.service;

import java.util.List;

import com.invoice.entity.InvoiceEntity;
import com.model.transaction.Invoice;

/**
 * @author vkamble
 *
 */
public interface InvoiceService {

	/**
	 * Method to save invoice
	 * @param invoice
	 * @return Invoice
	 * @throws Exception 
	 */
	public Invoice saveInvoice(Invoice invoice) throws Exception;
	
	/**
	 * Method to update invoice
	 * @param invoice
	 * @return Invoice
	 * @throws Exception 
	 */
	public Invoice updateInvoice(Invoice invoice) throws Exception;
	
	/**
	 * Method to get invoice by Id
	 * @param id
	 * @return Invoice
	 */
	public Invoice getInvoice(Long id);
	
	/**
	 * Method to check if invoice exists or not
	 * @param id
	 * @return boolean
	 */
	public boolean isInvoiceExists(Long id);
	
	/**
	 * Method to delete invoice
	 * @param id
	 */
	public void deleteInvoice(Long id);
	
	/**
	 * Method to get all invoices
	 * @return List<Invoice>
	 */
	public List<Invoice> getAllInvoices();
	
	/**
	 * Method to get all invoices by customerId
	 * @return List<Invoice>
	 */
	public List<Invoice> getAllInvoicesForCustomerId(Long customerId);

	/**
	 * Helper function to convert Invoice model to Invoice Entity
	 * @param invoice
	 * @return InvoiceEntity
	 */
	public InvoiceEntity getInvoiceEntityFromInvoiceModel(Invoice invoice);
	
	/**
	 * Helper function to convert Invoice entity to Invoice model
	 * @param invoiceEntity
	 * @return Invoice
	 */
	public Invoice getInvoiceModelFromInvoiceEntity(InvoiceEntity invoiceEntity);

}
