/**
 * 
 */
package com.invoice.unittest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.invoice.entity.InvoiceEntity;
import com.invoice.entity.ItemEntity;
import com.invoice.repository.InvoiceRepository;
import com.invoice.repository.ItemRepository;
import com.invoice.service.InvoiceService;
import com.invoice.service.InvoiceServiceImpl;
import com.invoice.service.ItemService;
import com.invoice.service.ItemServiceImpl;
import com.model.transaction.Item;

/**
 * @author vkamble
 *
 */
@RunWith(SpringRunner.class)
public class ItemServiceTest {

    @TestConfiguration
    static class ItemServiceImplTestContextConfiguration {
  
        @Bean
        public ItemService itemService() {
            return new ItemServiceImpl();
        }
        
        @Bean
        public InvoiceService InvoiceService() {
            return new InvoiceServiceImpl();
        }

    }
    	
	@Autowired
	private ItemService itemService;

	@Autowired
	private InvoiceService InvoiceService;

	@MockBean
	ItemRepository itemRepository;
	
	@MockBean
	InvoiceRepository InvoiceRepository;

	
	@Before
	public void setUp(){
		
		ItemEntity itemEntity1 = new ItemEntity();
		itemEntity1.setDescription("Item1");
		itemEntity1.setAmount(10);
		itemEntity1.setCreatedDate(new Date());
		itemEntity1.setUpdatedDate(new Date());

		ItemEntity itemEntity2 = new ItemEntity();
		itemEntity2.setDescription("Item1");
		itemEntity2.setAmount(10);
		itemEntity2.setCreatedDate(new Date());
		itemEntity2.setUpdatedDate(new Date());
		itemEntity2.setId(1);
		
		List<ItemEntity> itemEntities1= new ArrayList<ItemEntity>();
		itemEntities1.add(itemEntity2);
		
		Set<ItemEntity> itemEntities2= new HashSet<ItemEntity>();
		itemEntities2.add(itemEntity2);
		
		InvoiceEntity invoiceEntity2 = new InvoiceEntity();
		invoiceEntity2.setItems(itemEntities2);
		invoiceEntity2.setCustomerId(1L);
		invoiceEntity2.setCreatedDate(new Date());
		invoiceEntity2.setUpdatedDate(new Date());
		invoiceEntity2.setId(1);
		
		Mockito.when(InvoiceRepository.findOne(invoiceEntity2.getId())).thenReturn(invoiceEntity2);

		Mockito.when(itemRepository.exists(itemEntity2.getId())).thenReturn(true);

		Mockito.when(itemRepository.findOne(itemEntity2.getId())).thenReturn(itemEntity2);
		
		Mockito.when(itemRepository.findAll()).thenReturn(itemEntities1);
		
		Mockito.when(itemRepository.findByInvoiceEntity(invoiceEntity2)).thenReturn(itemEntities1);
		
		Mockito.when(itemRepository.save(Mockito.any(ItemEntity.class)))
        .thenAnswer(new Answer<ItemEntity>() {
            @Override
            public ItemEntity answer(InvocationOnMock invocation) throws Throwable {
            	ItemEntity itemEntity = (ItemEntity) invocation.getArguments()[0];
            	itemEntity.setId(1L);
                return itemEntity;
            }
        });		
	}
	
	

	@Test
	public void testSaveItem(){
		
		Item item = new Item();
		item.setDescription("Item1");
		item.setAmount(10);
		item.setCreatedDate(new Date());
		item.setUpdatedDate(new Date());
		
		item = itemService.saveItem(item);
		Assert.notNull(item,"Item is null");
		Assert.isTrue(item.getId()==1,"Item is not stored");
	}
	
	@Test
	public void testdeleteItemWithExistingId(){
		
		boolean result = itemService.deleteItem(1L);
		Assert.isTrue(result==true,"Item not found");
	}


	@Test
	public void testIsItemExistsWithValidId(){
		
		boolean result = itemService.isItemExists(1L);
		Assert.isTrue(result==true,"Item not found");
	}
	
	@Test
	public void testIsItemExistsWithInValidId(){
		
		boolean result = itemService.isItemExists(2L);
		Assert.isTrue(result==false,"Item found");
	}

	
	@Test
	public void testGetItemWithValidId(){
		
		Item item = itemService.getItem(1L);
		Assert.notNull(item,"Item is null");
		Assert.isTrue(item.getId()==1,"Item is null");
	}
	
	@Test
	public void testGetItemWithInValidId(){
		
		Item item = itemService.getItem(2L);
		Assert.isNull(item,"Item is not null");
	}

	@Test
	public void testGetAllItems(){
		
		List<Item> items = itemService.getAllItems();
		Assert.notNull(items,"Item list is null");
		Assert.isTrue(items.size()>0,"Item list is empty");
	}

	@Test
	public void testGetAllItemsByInvoiceId(){
		
		List<Item> items = itemService.getItemsByInvoiceId(1L);
		Assert.notNull(items,"Item list is null");
		Assert.isTrue(items.size()>0,"Item list is empty");
	}


}
