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
import com.invoice.repository.ItemRepository;
import com.model.transaction.Invoice;
import com.model.transaction.Item;

/**
 * @author vkamble
 *
 */
@Service
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Autowired
	InvoiceService invoiceService;
		
	/**
	 * Method to save item
	 * @param item
	 * @return Item
	 */
	@Override
	public Item saveItem(Item item) {
		ItemEntity itemEntity = getItemEntityFromItemModel(item);
		itemRepository.save(itemEntity);
		item=getItemModelFromItemEntity(itemEntity);
		return item;
	}
	
	/**
	 * Method to get item by Id
	 * @param id
	 * @return Item
	 */
	@Override
	public Item getItem(Long id) {
		
		ItemEntity itemEntity = itemRepository.findOne(id);
		if(itemEntity!=null)
			return getItemModelFromItemEntity(itemEntity);
		return null;
	}
	
	/**
	 * Method to check if item exists or not
	 * @param id
	 * @return boolean
	 */
	@Override
	public boolean isItemExists(Long id) {
		return itemRepository.exists(id);
	}

	/**
	 * Method to delete item
	 * @param id
	 * @throws Exception 
	 */
	@Override
	public void deleteItem(Long id) throws Exception {
		
			itemRepository.delete(id);
	}
	
	/**
	 * Method to get all items
	 * @return List<Item>
	 */
	@Override
	public List<Item> getAllItems() {
		Iterable<ItemEntity> itemEntities = itemRepository.findAll();
		List<Item> items = new ArrayList<>();
		if(itemEntities!=null){
			for(ItemEntity itemEntity:itemEntities){
				items.add(getItemModelFromItemEntity(itemEntity));
			}
		}
		return items;
	}
	
	/**
	 * Method to get items by invoiceId
	 * @return List<Item>
	 */
	@Override
	public List<Item> getItemsByInvoiceId(Long invoiceId) {
		List<Item> items = new ArrayList<>();
		InvoiceEntity invoiceEntity = invoiceRepository.findOne(invoiceId);
		if(invoiceEntity!=null){
			Iterable<ItemEntity> itemEntities = itemRepository.findByInvoiceEntity(invoiceEntity);
			if(itemEntities!=null){
				for(ItemEntity itemEntity:itemEntities){
					items.add(getItemModelFromItemEntity(itemEntity));
				}
			}
		}
		return items;
	}


	/**
	 * Helper function to convert Item model to Item Entity
	 * @param item
	 * @return ItemEntity
	 */
	public ItemEntity getItemEntityFromItemModel(Item item){
		ItemEntity itemEntity = new ItemEntity();
		itemEntity.setId(item.getId());
		itemEntity.setDescription(item.getDescription());
		itemEntity.setAmount(item.getAmount());
		itemEntity.setCreatedDate(item.getCreatedDate());
		itemEntity.setUpdatedDate(item.getUpdatedDate());
		return itemEntity;
	}
	
	/**
	 * Helper function to convert Item entity to Item model
	 * @param itemEntity
	 * @return Item
	 */
	public Item getItemModelFromItemEntity(ItemEntity itemEntity){
		Item item = new Item();
		item.setId(itemEntity.getId());
		item.setDescription(itemEntity.getDescription());
		item.setAmount(itemEntity.getAmount());
		item.setCreatedDate(itemEntity.getCreatedDate());
		item.setUpdatedDate(itemEntity.getUpdatedDate());
		return item;
	}

}
