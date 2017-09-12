/**
 * 
 */
package com.invoice.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.Boot;
import com.invoice.repository.ItemRepository;
import com.model.transaction.Invoice;
import com.model.transaction.Item;
import com.model.util.Response;
import com.model.util.ResponseCode;
import com.model.util.ResponseType;

/**
 * @author vkamble
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = Boot.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ItemServiceTest {
	
    @TestConfiguration
    static class ItemServiceImplTestContextConfiguration {
  
        @Bean
        public ItemService itemService() {
            return new ItemServiceImpl();
        }
    }
    
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ItemService itemService;

	@Autowired
	ItemRepository itemRepository;

    @Autowired
    ObjectMapper objectMapper;
    
    private Invoice invoice;
    
	@Test
	public void testCreateItem() throws Exception{
		
		
		Date date = new Date();
		Item item = new Item();
		item.setAmount(10);
		item.setCreatedDate(date);
		item.setDescription("Item1");		

		MvcResult result = mvc.perform(post("/item")
				.content(objectMapper.writeValueAsString(item))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemResult = objectMapper.convertValue(response.getObject(), Item.class);
		
		Assert.isTrue(itemResult.getId()>0, "Test failed while creating item");
	}
		
	@Test
	public void testCreateItemWithNullCreatedDateTest() throws JsonProcessingException, Exception{
		
		Item item = new Item();
		item.setAmount(10);
		item.setDescription("Item1");
		
		MvcResult result = mvc.perform(post("/item")
				.content(objectMapper.writeValueAsString(item))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.VALIDATION_ERROR),"Validation failed while creating item with null created date");

	}
		
	@Test
	public void testReadItem() throws Exception{
		
		Date date = new Date();
		Item item = new Item();
		item.setAmount(10);
		item.setCreatedDate(date);
		item.setDescription("Item1");
		
		MvcResult result = mvc.perform(post("/item")
				.content(objectMapper.writeValueAsString(item))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemCreateResult = objectMapper.convertValue(response.getObject(), Item.class);
		Assert.isTrue(itemCreateResult.getId()>0, "Test failed while creating item");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/item/{id}", itemCreateResult.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemReadResult = objectMapper.convertValue(response.getObject(), Item.class);
		Assert.isTrue(itemReadResult.getId()>0, "Test failed while reading item");
		Assert.isTrue(itemCreateResult.getId()==itemReadResult.getId(), "Test failed due to expcetd and actual id mis-match");
	}
	
	@Test
	public void testReadItemForNonExistingId() throws Exception{
				
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/item/{id}", new Long(1000))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DATA_NOT_FOUND_ERROR),"data not found error while reading item");
	}
	
	@Test
	public void testUpdateItem() throws Exception{
		
		Date date = new Date();
		Item item = new Item();
		item.setAmount(10);
		item.setCreatedDate(date);
		item.setDescription("Item1");

		MvcResult result = mvc.perform(post("/item")
				.content(objectMapper.writeValueAsString(item))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemCreateResult = objectMapper.convertValue(response.getObject(), Item.class);
		
		Assert.isTrue(itemCreateResult.getId()>0, "Test failed while creating item");
		
		itemCreateResult.setDescription("Item1_Updated");
		
		result = mvc.perform(put("/item")
				.content(objectMapper.writeValueAsString(itemCreateResult))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemUpdateResult = objectMapper.convertValue(response.getObject(), Item.class);
		Assert.isTrue(itemUpdateResult.getId()>0, "Test failed while reading updated item");
		Assert.isTrue(itemCreateResult.getId()==itemUpdateResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(itemCreateResult.getDescription().equalsIgnoreCase("Item1_Updated"), "Test failed due to expcetd and actual description mis-match");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/item/{id}", itemUpdateResult.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemReadResult = objectMapper.convertValue(response.getObject(), Item.class);
		Assert.isTrue(itemReadResult.getId()>0, "Test failed while reading item");
		Assert.isTrue(itemReadResult.getId()==itemReadResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(itemReadResult.getDescription().equalsIgnoreCase("Item1_Updated"), "Test failed due to expcetd and actual description mis-match");
	}


	@Test
	public void testUpdateItemBySettingNullCreatedDate() throws Exception{
		
		Date date = new Date();
		Item item = new Item();
		item.setAmount(10);
		item.setCreatedDate(date);
		item.setDescription("Item1");

		MvcResult result = mvc.perform(post("/item")
				.content(objectMapper.writeValueAsString(item))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemCreateResult = objectMapper.convertValue(response.getObject(), Item.class);
		
		Assert.isTrue(itemCreateResult.getId()>0, "Test failed while creating item");
		
		itemCreateResult.setCreatedDate(null);
		
		result = mvc.perform(put("/item")
				.content(objectMapper.writeValueAsString(itemCreateResult))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a item response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.VALIDATION_ERROR),"Validation failed while creating item with null name");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/item/{id}", itemCreateResult.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemReadResult = objectMapper.convertValue(response.getObject(), Item.class);
		Assert.isTrue(itemReadResult.getId()>0, "Test failed while reading item");
		Assert.isTrue(itemReadResult.getId()==itemReadResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(itemReadResult.getCreatedDate()!=null, "Test failed due to expcetd and actual id mis-match");
	}
	

	@Test
	public void testDeleteItem() throws Exception{
		
		Date date = new Date();
		Item item = new Item();
		item.setAmount(10);
		item.setCreatedDate(date);
		item.setDescription("Item1");

		MvcResult result = mvc.perform(post("/item")
				.content(objectMapper.writeValueAsString(item))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemCreateResult = objectMapper.convertValue(response.getObject(), Item.class);
		
		Assert.isTrue(itemCreateResult.getId()>0, "Test failed while creating item");
				
		result = mvc.perform(MockMvcRequestBuilders.delete("/item/{id}", itemCreateResult.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.STATUS,"Not a item response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DELETE_SUCCESS),"Validation failed while deleteing item");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/item/{id}", itemCreateResult.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DATA_NOT_FOUND_ERROR),"data not found error while reading item");
	}

	@Test
	public void testDeeleteItemForNonExistingId() throws Exception{
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/item/{id}", new Long(1000))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DATA_NOT_FOUND_ERROR),"data not found error while reading item");
	}


	@Test
	public void testReadAllItems() throws Exception{
		
		Date date = new Date();
		Item item1 = new Item();
		item1.setAmount(10);
		item1.setCreatedDate(date);
		item1.setDescription("Item1");

		MvcResult result = mvc.perform(post("/item")
				.content(objectMapper.writeValueAsString(item1))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemCreateResult1 = objectMapper.convertValue(response.getObject(), Item.class);
		Assert.isTrue(itemCreateResult1.getId()>0, "Test failed while creating item");

		Item item2 = new Item();
		item2.setAmount(10);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		result = mvc.perform(post("/item")
				.content(objectMapper.writeValueAsString(item2))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a item response");
		Item itemCreateResult2 = objectMapper.convertValue(response.getObject(), Item.class);
		Assert.isTrue(itemCreateResult2.getId()>0, "Test failed while creating item");

		result = mvc.perform(MockMvcRequestBuilders.get("/item")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a invoice response");
		List<Item> myObjects = objectMapper.convertValue(response.getObject(), new TypeReference<List<Item>>(){});

		Assert.isTrue(myObjects.size()>0, "Test failed while reading all item");
	}

	
//	@Test @Transactional
//	public void testReadItemsByInvoiceId() throws Exception{
//		
//		Date date = new Date();
//		Customer customer = new Customer();
//		customer.setName("Vaibhav");
//		customer.setEmailId("test@test.com");
//		customer.setCreatedDate(date);
//		customer.setId(1);
//		
//		Item item1 = new Item();
//		item1.setAmount(10);
//		item1.setCreatedDate(date);
//		item1.setDescription("Item1");
//
//		Item item2 = new Item();
//		item2.setAmount(15);
//		item2.setCreatedDate(date);
//		item2.setDescription("Item2");
//
//		Invoice invoice = new Invoice();
//		invoice.setAmount(0);
//		invoice.setCustomer(customer);
//		invoice.setCreatedDate(date);
//		invoice.addItem(item1);
//		invoice.addItem(item2);
//		
//		MvcResult result = mvc.perform(post("/invoice")
//				.content(objectMapper.writeValueAsString(invoice))
//				.accept(MediaType.APPLICATION_JSON)
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
//		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
//		Invoice invoiceResult = objectMapper.convertValue(response.getObject(), Invoice.class);
//		
//		Assert.isTrue(invoiceResult.getId()>0, "Test failed while creating invoice");
//		Assert.isTrue(invoiceResult.getAmount()==25, "Test failed while creating invoice");
//		
//		result = mvc.perform(MockMvcRequestBuilders.get("/item/invoice").param("id", invoiceResult.getId().toString())
//				.accept(MediaType.APPLICATION_JSON)
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
//		Assert.isTrue(response.getResponseType()==ResponseType.ITEM,"Not a invoice response");
//		List<Item> myObjects = objectMapper.convertValue(response.getObject(), new TypeReference<List<Item>>(){});
//		Assert.isTrue(myObjects.size()==2, "Test failed while reading all items");
//		Assert.isTrue(myObjects.get(0).getId()==invoiceResult.getItems().get(0).getId() || myObjects.get(0).getId()==invoiceResult.getItems().get(1).getId(), "Test failed due to item id mis-match");
//		Assert.isTrue(myObjects.get(1).getId()==invoiceResult.getItems().get(0).getId() || myObjects.get(1).getId()==invoiceResult.getItems().get(1).getId(), "Test failed due to item id mis-match");
//	}


}
