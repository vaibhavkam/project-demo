/**
 * 
 */
package com.invoice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.invoice.entity.InvoiceEntity;

/**
 * @author vkamble
 *
 */
public interface InvoiceRepository extends CrudRepository<InvoiceEntity,Long> {

	List<InvoiceEntity> findByCustomerId(Long customerId);
	
}
