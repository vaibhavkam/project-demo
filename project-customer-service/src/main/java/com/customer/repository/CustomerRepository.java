/**
 * 
 */
package com.customer.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.customer.entity.CustomerEntity;

/**
 * @author vkamble
 * Customer repository with in place functions to achieve data base operations
 */
@Repository
public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity,Long> {

	
}
