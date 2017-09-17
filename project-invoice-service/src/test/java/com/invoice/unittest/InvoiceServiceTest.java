/**
 * 
 */
package com.invoice.unittest;

import static org.junit.Assert.fail;

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
import com.model.transaction.Invoice;
import com.model.transaction.Item;
import com.model.user.Customer;


/**
 * @author vkamble
 *
 */
@RunWith(SpringRunner.class)
public class InvoiceServiceTest {

    @TestConfiguration
    static class InvoiceServiceImplTestContextConfiguration {
  
        @Bean
        public InvoiceService InvoiceService() {
            return new InvoiceServiceImpl();
        }
        
        @Bean
        public ItemService ItemService() {
            return new ItemServiceImpl();
        }

    }
    	
	@Autowired
	private InvoiceService InvoiceService;
	
	@Autowired
	private ItemService ItemService;

	@MockBean
	InvoiceRepository InvoiceRepository;

	@MockBean
	ItemRepository ItemRepository;

	@Before
	public void setUp(){
		
		ItemEntity itemEntity = new ItemEntity();
		itemEntity.setDescription("Item1");
		itemEntity.setAmount(10);
		itemEntity.setCreatedDate(new Date());
		itemEntity.setUpdatedDate(new Date());
		
		Set<ItemEntity> itemEntities = new HashSet<ItemEntity>();
		itemEntities.add(itemEntity);

		InvoiceEntity invoiceEntity1 = new InvoiceEntity();
		invoiceEntity1.setItems(itemEntities);
		invoiceEntity1.setCustomerId(1L);
		invoiceEntity1.setCreatedDate(new Date());
		invoiceEntity1.setUpdatedDate(new Date());

		InvoiceEntity invoiceEntity2 = new InvoiceEntity();
		invoiceEntity2.setItems(itemEntities);
		invoiceEntity2.setCustomerId(1L);
		invoiceEntity2.setCreatedDate(new Date());
		invoiceEntity2.setUpdatedDate(new Date());
		invoiceEntity2.setId(1);

		List<InvoiceEntity> InvoiceEntities= new ArrayList<InvoiceEntity>();
		InvoiceEntities.add(invoiceEntity2);

		Mockito.when(InvoiceRepository.exists(invoiceEntity2.getId())).thenReturn(true);

		Mockito.when(InvoiceRepository.findOne(invoiceEntity2.getId())).thenReturn(invoiceEntity2);
		
		Mockito.when(InvoiceRepository.findAll()).thenReturn(InvoiceEntities);
		
		Mockito.when(InvoiceRepository.findByCustomerId(1L)).thenReturn(InvoiceEntities);
		
		Mockito.when(InvoiceRepository.save(Mockito.any(InvoiceEntity.class)))
        .thenAnswer(new Answer<InvoiceEntity>() {
            @Override
            public InvoiceEntity answer(InvocationOnMock invocation) throws Throwable {
            	InvoiceEntity InvoiceEntity = (InvoiceEntity) invocation.getArguments()[0];
            	InvoiceEntity.setId(1L);
                return InvoiceEntity;
            }
        });		
	}
	
	

	@Test
	public void testSaveInvoice(){
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());
		customer.setUpdatedDate(new Date());
		customer.setId(1L);

		Item item = new Item();
		item.setDescription("Item1");
		item.setAmount(10);
		item.setCreatedDate(new Date());
		item.setUpdatedDate(new Date());
		
		List<Item> items = new ArrayList<Item>();
		items.add(item);

		Invoice invoice = new Invoice();
		invoice.setCustomer(customer);
		invoice.setItems(items);
		invoice.setCreatedDate(new Date());
		invoice.setUpdatedDate(new Date());
		
		try {
			invoice= InvoiceService.saveInvoice(invoice);
			Assert.notNull(invoice,"Invoice is null");
			Assert.isTrue(invoice.getId()==1,"Invoice is not stored");
		} catch (Exception e) {
			if(e.getMessage().equalsIgnoreCase("Invoice should contains atleast one item"))
				fail("test failed with empty item list");
			else
				fail("test failed");
		}
	}
	
	@Test
	public void testUpdateInvoice(){
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());
		customer.setUpdatedDate(new Date());
		customer.setId(1L);

		Item item = new Item();
		item.setDescription("Item1");
		item.setAmount(10);
		item.setCreatedDate(new Date());
		item.setUpdatedDate(new Date());
		
		List<Item> items = new ArrayList<Item>();
		items.add(item);

		Invoice invoice = new Invoice();
		invoice.setCustomer(customer);
		invoice.setItems(items);
		invoice.setCreatedDate(new Date());
		invoice.setUpdatedDate(new Date());
		invoice.setId(1L);
		
		try {
			invoice= InvoiceService.updateInvoice(invoice);
			Assert.notNull(invoice,"Invoice is null");
			Assert.isTrue(invoice.getId()==1,"Invoice is not stored");
		} catch (Exception e) {
			if(e.getMessage().equalsIgnoreCase("Invoice should contains atleast one item"))
				fail("test failed with empty item list");
			else
				fail("test failed");
		}
	}
	
	
	
	@Test(expected=Exception.class)
	public void testSaveInvoiceWithEmptyItemList() throws Exception{
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());
		customer.setUpdatedDate(new Date());
		customer.setId(1L);

		Invoice invoice = new Invoice();
		invoice.setCustomer(customer);
		invoice.setCreatedDate(new Date());
		invoice.setUpdatedDate(new Date());
		invoice= InvoiceService.saveInvoice(invoice);
	}
	
	@Test
	public void testdeleteInvoiceWithExistingId(){
		
		boolean result = InvoiceService.deleteInvoice(1L);
		Assert.isTrue(result==true,"Invoice not found");
	}


	@Test
	public void testIsInvoiceExistsWithValidId(){
		
		boolean result = InvoiceService.isInvoiceExists(1L);
		Assert.isTrue(result==true,"Invoice not found");
	}
	
	@Test
	public void testIsInvoiceExistsWithInValidId(){
		
		boolean result = InvoiceService.isInvoiceExists(2L);
		Assert.isTrue(result==false,"Invoice found");
	}

	
	@Test
	public void testGetInvoiceWithValidId(){
		
		Invoice Invoice = InvoiceService.getInvoice(1L);
		Assert.notNull(Invoice,"Invoice is null");
		Assert.isTrue(Invoice.getId()==1,"Invoice is null");
	}
	
	@Test
	public void testGetInvoiceWithInValidId(){
		
		Invoice Invoice = InvoiceService.getInvoice(2L);
		Assert.isNull(Invoice,"Invoice is not null");
	}

	@Test
	public void testGetAllInvoices(){
		
		List<Invoice> Invoices = InvoiceService.getAllInvoices();
		Assert.notNull(Invoices,"Invoice list is null");
		Assert.isTrue(Invoices.size()>0,"Invoice list is empty");
	}

	@Test
	public void testGetAllInvoicesForCustomerId(){
		
		List<Invoice> Invoices = InvoiceService.getAllInvoicesForCustomerId(1L);
		Assert.notNull(Invoices,"Invoice list is null");
		Assert.isTrue(Invoices.size()>0,"Invoice list is empty");
	}


}
