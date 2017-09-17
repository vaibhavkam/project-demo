/**
 * 
 */
package com.invoice.service;

import java.util.List;

import com.invoice.entity.ItemEntity;
import com.model.transaction.Item;

/**
 * @author vkamble
 *
 */
public interface ItemService {
	/**
	 * Method to save item
	 * @param item
	 * @return Item
	 */
	public Item saveItem(Item item);
	
	/**
	 * Method to get item by Id
	 * @param id
	 * @return Item
	 */
	public Item getItem(Long id);
	
	/**
	 * Method to check if item exists or not
	 * @param id
	 * @return boolean
	 */
	public boolean isItemExists(Long id);
	
	/**
	 * Method to delete item
	 * @param id
	 * @return 
	 */
	public boolean deleteItem(Long id);
	
	/**
	 * Method to get all items
	 * @return List<Item>
	 */
	public List<Item> getAllItems();
	
	/**
	 * Method to get items by invoiceId
	 * @return List<Item>
	 */
	public List<Item> getItemsByInvoiceId(Long invoiceId);
	
	/**
	 * Helper function to convert Item model to Item Entity
	 * @param item
	 * @return ItemEntity
	 */
	public ItemEntity getItemEntityFromItemModel(Item item);
	
	/**
	 * Helper function to convert Item entity to Item model
	 * @param itemEntity
	 * @return Item
	 */
	public Item getItemModelFromItemEntity(ItemEntity itemEntity);
}
