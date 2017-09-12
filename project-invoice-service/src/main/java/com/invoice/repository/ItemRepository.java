/**
 * 
 */
package com.invoice.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.invoice.entity.InvoiceEntity;
import com.invoice.entity.ItemEntity;

/**
 * @author vkamble
 *
 */
@Repository
public interface ItemRepository extends PagingAndSortingRepository<ItemEntity,Long> {

	List<ItemEntity> findByInvoiceEntity(InvoiceEntity invoiceEntity);

}
