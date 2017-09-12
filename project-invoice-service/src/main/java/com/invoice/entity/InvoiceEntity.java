/**
 * 
 */
package com.invoice.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author vkamble
 *
 */
@Entity
public class InvoiceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false)
	private long customerId;
		
	@OneToMany(mappedBy = "invoiceEntity", cascade = CascadeType.ALL)
	private Set<ItemEntity> itemEntities;
	
	@Column(nullable=false)
	private Date createdDate;
	private Date updatedDate;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the customerId
	 */
	public long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the items
	 */
	public Set<ItemEntity> getItems() {
		return itemEntities;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(Set<ItemEntity> items) {
		this.itemEntities = items;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void addItem(ItemEntity item) {
		if(itemEntities==null)
			itemEntities = new HashSet<ItemEntity>();
		itemEntities.add(item);
	}

	public boolean deleteItem(ItemEntity item) {
		if(itemEntities==null || !itemEntities.contains(item))
			return false;
		return itemEntities.remove(item);
	}

}
