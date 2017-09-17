/**
 * 
 */
package com.invoice.integrationtest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.Boot;
import com.invoice.repository.InvoiceRepository;
import com.invoice.service.InvoiceService;
import com.invoice.service.InvoiceServiceImpl;
import com.model.transaction.Invoice;
import com.model.transaction.Item;
import com.model.user.Customer;
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
public class InvoiceIntegrationTest {

    @TestConfiguration
    static class InvoiceServiceImplTestContextConfiguration {
  
        @Bean
        public InvoiceService invoiceService() {
            return new InvoiceServiceImpl();
        }
        
    }
    
	@Autowired
	private MockMvc mvc;
		
	@Autowired
	InvoiceRepository invoiceRepository;

    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    private Environment env;
            
    public static String authHeaderValue;

    @Before
    public void setUp(){
    	
    	authHeaderValue = "Basic " + new String(Base64.encodeBase64((env.getProperty("validConsumerUserName")+":"+env.getProperty("validConsumerPassword")).getBytes()));
    }
    
	@Test
	public void testCreateInvoice() throws Exception{
		
		Date date = new Date();
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(date);
		customer.setId(1);
		
		Item item1 = new Item();
		item1.setAmount(10);
		item1.setCreatedDate(date);
		item1.setDescription("Item1");

		Item item2 = new Item();
		item2.setAmount(15);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.setCustomer(customer);
		invoice.setCreatedDate(date);
		invoice.addItem(item1);
		invoice.addItem(item2);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceResult = objectMapper.convertValue(response.getObject(), Invoice.class);
		Assert.isTrue(invoiceResult.getId()>0, "Test failed while creating invoice");
		Assert.isTrue(invoiceResult.getAmount()==25, "Test failed while creating invoice");

	}

	@Test
	public void testCreateInvoiceWithNullCreatedDate() throws Exception{
		
		Date date = new Date();
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(date);
		customer.setId(1);
		
		Item item1 = new Item();
		item1.setAmount(10);
		item1.setCreatedDate(date);
		item1.setDescription("Item1");

		Item item2 = new Item();
		item2.setAmount(15);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.setCustomer(customer);
		invoice.addItem(item1);
		invoice.addItem(item2);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.VALIDATION_ERROR),"Validation failed while creating invoice with null created date");
	}

	@Test
	public void testCreateInvoiceWithNullCustomer() throws Exception{
		
		Date date = new Date();	
		
		Item item1 = new Item();
		item1.setAmount(10);
		item1.setCreatedDate(date);
		item1.setDescription("Item1");

		Item item2 = new Item();
		item2.setAmount(15);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.addItem(item1);
		invoice.addItem(item2);
		invoice.setCreatedDate(date);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.VALIDATION_ERROR),"Validation failed while creating invoice with null customer");
	}

	@Test
	public void testCreateInvoiceWithItemsHavingNullCreatedDate() throws Exception{
		
		Date date = new Date();	
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(date);
		customer.setId(1);

		Item item1 = new Item();
		item1.setAmount(10);
		item1.setDescription("Item1");

		Item item2 = new Item();
		item2.setAmount(15);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.addItem(item1);
		invoice.addItem(item2);
		invoice.setCreatedDate(date);
		invoice.setCustomer(customer);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.CREATE_ERROR),"error occured while creating invoice with items having null created date");
	}

	@Test
	public void testCreateInvoiceWithoutItems() throws Exception{
		
		Date date = new Date();
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(date);
		customer.setId(1);
		
		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.setCustomer(customer);
		invoice.setCreatedDate(date);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.CREATE_ERROR),"error occured while creating invoice with items having null created date");
	}
	
	@Test
	public void testReadInvoice() throws Exception{
		
		Date date = new Date();
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(date);
		customer.setId(1);
		
		Item item1 = new Item();
		item1.setAmount(10);
		item1.setCreatedDate(date);
		item1.setDescription("Item1");

		Item item2 = new Item();
		item2.setAmount(15);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.setCustomer(customer);
		invoice.setCreatedDate(date);
		invoice.addItem(item1);
		invoice.addItem(item2);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceCreateResult = objectMapper.convertValue(response.getObject(), Invoice.class);


		Assert.isTrue(invoiceCreateResult.getId()>0, "Test failed while creating invoice");
		Assert.isTrue(invoiceCreateResult.getAmount()==25, "Test failed while creating invoice");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/invoice/{id}", invoiceCreateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceReadResult = objectMapper.convertValue(response.getObject(), Invoice.class);

		
		Assert.isTrue(invoiceReadResult.getId()>0, "Test failed while reading invoice");
		Assert.isTrue(invoiceReadResult.getId()==invoiceReadResult.getId(), "Test failed due to expcetd and actual id mis-match");
	}

	@Test
	public void testReadInvoiceForNonExistingId() throws Exception{
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/invoice/{id}", new Long(1000))
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DATA_NOT_FOUND_ERROR),"data not found error while reading invoice");
	}

	@Test
	public void testUpdateInvoiceSetNewCustomer() throws Exception{
		
		Date date = new Date();
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(date);
		customer.setId(1);
		
		Item item1 = new Item();
		item1.setAmount(10);
		item1.setCreatedDate(date);
		item1.setDescription("Item1");

		Item item2 = new Item();
		item2.setAmount(15);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.setCustomer(customer);
		invoice.setCreatedDate(date);
		
		invoice.addItem(item1);
		invoice.addItem(item2);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceCreateResult = objectMapper.convertValue(response.getObject(), Invoice.class);
		
		Assert.isTrue(invoiceCreateResult.getId()>0, "Test failed while creating invoice");
		Assert.isTrue(invoiceCreateResult.getAmount()==25, "Test failed while creating invoice");
		
		Customer customer1 = new Customer();
		customer1.setName("Vaibhav");
		customer1.setEmailId("test@test.com");
		customer1.setCreatedDate(date);
		customer1.setId(2);

		invoiceCreateResult.setCustomer(customer1);
		
		result = mvc.perform(put("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoiceCreateResult))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceUpdateResult = objectMapper.convertValue(response.getObject(), Invoice.class);

		Assert.isTrue(invoiceUpdateResult.getId()>0, "Test failed while reading updated customer");
		Assert.isTrue(invoiceUpdateResult.getId()==invoiceUpdateResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(invoiceUpdateResult.getCustomer().getId()==customer1.getId(), "Test failed due to expcetd and actual customer id mis-match");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/invoice/{id}", invoiceUpdateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceReadResult = objectMapper.convertValue(response.getObject(), Invoice.class);
		
		Assert.isTrue(invoiceReadResult.getId()>0, "Test failed while reading customer");
		Assert.isTrue(invoiceReadResult.getId()==invoiceReadResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(invoiceReadResult.getCustomer().getId()==customer1.getId(), "Test failed due to expcetd and actual customer id mis-match");
	}

	@Test
	public void testUpdateInvoiceAddNewItems() throws Exception{
		
		Date date = new Date();
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(date);
		customer.setId(1);
		
		Item item1 = new Item();
		item1.setAmount(10);
		item1.setCreatedDate(date);
		item1.setDescription("Item1");

		Item item2 = new Item();
		item2.setAmount(15);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.setCustomer(customer);
		invoice.setCreatedDate(date);
		invoice.addItem(item1);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceCreateResult = objectMapper.convertValue(response.getObject(), Invoice.class);
		
		Assert.isTrue(invoiceCreateResult.getId()>0, "Test failed while creating invoice");
		Assert.isTrue(invoiceCreateResult.getAmount()==10, "Test failed while creating invoice");
				
		invoiceCreateResult.addItem(item2);
		
		result = mvc.perform(put("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoiceCreateResult))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceUpdateResult = objectMapper.convertValue(response.getObject(), Invoice.class);
		
		Assert.isTrue(invoiceUpdateResult.getId()>0, "Test failed while reading updated customer");
		Assert.isTrue(invoiceUpdateResult.getId()==invoiceUpdateResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(invoiceUpdateResult.getItems().size()==2, "Test failed due to expcetd and actual invoice id mis-match");
		Assert.isTrue(invoiceUpdateResult.getAmount()==25, "Test failed due to expcetd and actual invoice id mis-match");

		
		result = mvc.perform(MockMvcRequestBuilders.get("/invoice/{id}", invoiceUpdateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceReadResult = objectMapper.convertValue(response.getObject(), Invoice.class);
		
		Assert.isTrue(invoiceReadResult.getId()>0, "Test failed while reading customer");
		Assert.isTrue(invoiceReadResult.getId()==invoiceReadResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(invoiceReadResult.getAmount()==25, "Test failed while reading invoice");
		Assert.isTrue(invoiceReadResult.getItems().size()==2, "Test failed due to expcetd and actual customer id mis-match");
	}
	
	@Test
	public void testDeleteInvoice() throws Exception{
		
		Date date = new Date();
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(date);
		customer.setId(1);
		
		Item item1 = new Item();
		item1.setAmount(10);
		item1.setCreatedDate(date);
		item1.setDescription("Item1");

		Item item2 = new Item();
		item2.setAmount(15);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.setCustomer(customer);
		invoice.setCreatedDate(date);
		invoice.addItem(item1);
		invoice.addItem(item2);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceCreateResult = objectMapper.convertValue(response.getObject(), Invoice.class);
		
		Assert.isTrue(invoiceCreateResult.getId()>0, "Test failed while creating invoice");
		Assert.isTrue(invoiceCreateResult.getAmount()==25, "Test failed while creating invoice");

		
		result = mvc.perform(MockMvcRequestBuilders.delete("/invoice/{id}", invoiceCreateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.STATUS,"Not a status response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DELETE_SUCCESS),"Validation failed while invoice customer");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/invoice/{id}", invoiceCreateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DATA_NOT_FOUND_ERROR),"data not found error while reading invoice");
	}
	
	@Test
	public void testDeleteInvoiceNonExistingInvoice() throws Exception{
		
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/invoice/{id}", new Long(1000))
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DATA_NOT_FOUND_ERROR),"data not found error while reading invoice");
	}

	@Test
	public void testGetAllInvoice() throws Exception{
		
		Date date = new Date();
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(date);
		customer.setId(1);
		
		Item item1 = new Item();
		item1.setAmount(10);
		item1.setCreatedDate(date);
		item1.setDescription("Item1");

		Item item2 = new Item();
		item2.setAmount(15);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.setCustomer(customer);
		invoice.setCreatedDate(date);
		invoice.addItem(item1);
		invoice.addItem(item2);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceCreateResult = objectMapper.convertValue(response.getObject(), Invoice.class);
		
		Assert.isTrue(invoiceCreateResult.getId()>0, "Test failed while creating invoice");
		Assert.isTrue(invoiceCreateResult.getAmount()==25, "Test failed while creating invoice");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/invoice")
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		List<Invoice> myObjects = objectMapper.convertValue(response.getObject(), new TypeReference<List<Invoice>>(){});

		Assert.isTrue(myObjects.size()>0, "Test failed while reading all invoice");
	}

	@Test
	public void testGetAllInvoiceByCustomer() throws Exception{
		
		Date date = new Date();
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(date);
		customer.setId(1);
		
		Item item1 = new Item();
		item1.setAmount(10);
		item1.setCreatedDate(date);
		item1.setDescription("Item1");

		Item item2 = new Item();
		item2.setAmount(15);
		item2.setCreatedDate(date);
		item2.setDescription("Item2");

		Invoice invoice = new Invoice();
		invoice.setAmount(0);
		invoice.setCustomer(customer);
		invoice.setCreatedDate(date);
		invoice.addItem(item1);
		invoice.addItem(item2);

		MvcResult result = mvc.perform(post("/invoice")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(invoice))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		Invoice invoiceCreateResult = objectMapper.convertValue(response.getObject(), Invoice.class);
		
		Assert.isTrue(invoiceCreateResult.getId()>0, "Test failed while creating invoice");
		Assert.isTrue(invoiceCreateResult.getAmount()==25, "Test failed while creating invoice");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/invoice/customer").param("id", "1")
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.INVOICE,"Not a invoice response");
		List<Invoice> myObjects = objectMapper.convertValue(response.getObject(), new TypeReference<List<Invoice>>(){});
		for(Invoice invoice2:myObjects){
			Assert.isTrue(invoice2.getCustomer().getId()==1, "Test failed due to customer id mis-match");
		}
	}
}
