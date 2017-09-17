/**
 * 
 */
package com.customer.integrationtest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
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

import com.customer.Boot;
import com.customer.repository.CustomerRepository;
import com.customer.service.CustomerService;
import com.customer.service.CustomerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CustomerIntegrationTest {
	
    @TestConfiguration
    static class CustomerServiceImplTestContextConfiguration {
  
        @Bean
        public CustomerService customerService() {
            return new CustomerServiceImpl();
        }
    }
    
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	CustomerRepository customerRepository;

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
	public void testCreateCustomer() throws Exception{
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());;

		MvcResult result = mvc.perform(post("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customer))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerResult = objectMapper.convertValue(response.getObject(), Customer.class);
		Assert.isTrue(customerResult.getId()>0, "Test failed while creating customer");
	}
	
	@Test
	public void testCreateCustomerWithNullNameTest() throws JsonProcessingException, Exception{
		
		Customer customer = new Customer();
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());;
				
		MvcResult result = mvc.perform(post("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customer))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.VALIDATION_ERROR),"Validation failed while creating customer with null name");

	}
	
	@Test
	public void testCreateCustomerWithNullCreatedDateTest() throws JsonProcessingException, Exception{
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
				
		MvcResult result = mvc.perform(post("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customer))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.VALIDATION_ERROR),"Validation failed while creating customer with null name");

	}
		
	@Test
	public void testReadCustomer() throws Exception{
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());;

		MvcResult result = mvc.perform(post("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customer))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerCreateResult = objectMapper.convertValue(response.getObject(), Customer.class);
		Assert.isTrue(customerCreateResult.getId()>0, "Test failed while creating customer");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/customer/{id}", customerCreateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerReadResult = objectMapper.convertValue(response.getObject(), Customer.class);
		Assert.isTrue(customerReadResult.getId()>0, "Test failed while reading customer");
		Assert.isTrue(customerCreateResult.getId()==customerReadResult.getId(), "Test failed due to expcetd and actual id mis-match");
	}
	
	@Test
	public void testReadCustomerForNonExistingId() throws Exception{
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());;
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/customer/{id}", new Long(1000))
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DATA_NOT_FOUND_ERROR),"data not found error while reading customer");
	}
	
	@Test
	public void testUpdateCustomer() throws Exception{
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());;

		MvcResult result = mvc.perform(post("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customer))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerCreateResult = objectMapper.convertValue(response.getObject(), Customer.class);
		
		Assert.isTrue(customerCreateResult.getId()>0, "Test failed while creating customer");
		
		customerCreateResult.setEmailId("updateEmailId@test.com");
		
		result = mvc.perform(put("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customerCreateResult))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerUpdateResult = objectMapper.convertValue(response.getObject(), Customer.class);
		Assert.isTrue(customerUpdateResult.getId()>0, "Test failed while reading updated customer");
		Assert.isTrue(customerCreateResult.getId()==customerUpdateResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(customerCreateResult.getEmailId().equalsIgnoreCase("updateEmailId@test.com"), "Test failed due to expcetd and actual id mis-match");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/customer/{id}", customerUpdateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerReadResult = objectMapper.convertValue(response.getObject(), Customer.class);
		Assert.isTrue(customerReadResult.getId()>0, "Test failed while reading customer");
		Assert.isTrue(customerUpdateResult.getId()==customerReadResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(customerReadResult.getEmailId().equalsIgnoreCase("updateEmailId@test.com"), "Test failed due to expcetd and actual id mis-match");
	}

	@Test
	public void testUpdateCustomerBySettingNullName() throws Exception{
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());;

		MvcResult result = mvc.perform(post("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customer))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerCreateResult = objectMapper.convertValue(response.getObject(), Customer.class);
		
		Assert.isTrue(customerCreateResult.getId()>0, "Test failed while creating customer");
		
		customerCreateResult.setName(null);
		
		result = mvc.perform(put("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customerCreateResult))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.VALIDATION_ERROR),"Validation failed while creating customer with null name");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/customer/{id}", customerCreateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerReadResult = objectMapper.convertValue(response.getObject(), Customer.class);
		Assert.isTrue(customerReadResult.getId()>0, "Test failed while reading customer");
		Assert.isTrue(customerReadResult.getId()==customerReadResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(customerReadResult.getName().equalsIgnoreCase("Vaibhav"), "Test failed due to expcetd and actual id mis-match");
	}

	@Test
	public void testUpdateCustomerBySettingNullCreatedDate() throws Exception{
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());

		MvcResult result = mvc.perform(post("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customer))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerCreateResult = objectMapper.convertValue(response.getObject(), Customer.class);
		
		Assert.isTrue(customerCreateResult.getId()>0, "Test failed while creating customer");
		
		customerCreateResult.setCreatedDate(null);
		
		result = mvc.perform(put("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customerCreateResult))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.VALIDATION_ERROR),"Validation failed while creating customer with null name");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/customer/{id}", customerCreateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerReadResult = objectMapper.convertValue(response.getObject(), Customer.class);
		Assert.isTrue(customerReadResult.getId()>0, "Test failed while reading customer");
		Assert.isTrue(customerReadResult.getId()==customerReadResult.getId(), "Test failed due to expcetd and actual id mis-match");
		Assert.isTrue(customerReadResult.getCreatedDate()!=null, "Test failed due to expcetd and actual id mis-match");
	}
	
	@Test
	public void testDeleteCustomer() throws Exception{
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());;

		MvcResult result = mvc.perform(post("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customer))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerCreateResult = objectMapper.convertValue(response.getObject(), Customer.class);
		
		Assert.isTrue(customerCreateResult.getId()>0, "Test failed while creating customer");
		
		customerCreateResult.setEmailId("updateEmailId@test.com");
		
		result = mvc.perform(MockMvcRequestBuilders.delete("/customer/{id}", customerCreateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.STATUS,"Not a status response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DELETE_SUCCESS),"Validation failed while deleteing customer");
		
		result = mvc.perform(MockMvcRequestBuilders.get("/customer/{id}", customerCreateResult.getId())
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DATA_NOT_FOUND_ERROR),"data not found error while reading customer");
	}

	@Test
	public void testDeleteCustomerForNonExistingId() throws Exception{
		
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/customer/{id}", new Long(1000))
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.ERROR,"Not a error response");
		Assert.isTrue(response.getResponseCode().equals(ResponseCode.DATA_NOT_FOUND_ERROR),"data not found error while reading customer");
	}

	@Test
	public void testReadAllCustomers() throws Exception{
		
		Customer customer1 = new Customer();
		customer1.setName("Vaibhav");
		customer1.setEmailId("test@test.com");
		customer1.setCreatedDate(new Date());

		MvcResult result = mvc.perform(post("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customer1))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		Response response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerCreateResult1 = objectMapper.convertValue(response.getObject(), Customer.class);
		Assert.isTrue(customerCreateResult1.getId()>0, "Test failed while creating customer");

		Customer customer2 = new Customer();
		customer2.setName("Test2");
		customer2.setEmailId("test2@test.com");
		customer2.setCreatedDate(new Date());

		result = mvc.perform(post("/customer")
				.header("Authorization", authHeaderValue)
				.content(objectMapper.writeValueAsString(customer1))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Response>(){});
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		Customer customerCreateResult2 = objectMapper.convertValue(response.getObject(), Customer.class);
		Assert.isTrue(customerCreateResult2.getId()>0, "Test failed while creating customer");

		result = mvc.perform(MockMvcRequestBuilders.get("/customer")
				.header("Authorization", authHeaderValue)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		
		response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);
		Assert.isTrue(response.getResponseType()==ResponseType.CUSTOMER,"Not a customer response");
		List<Customer> myObjects = objectMapper.convertValue(response.getObject(), new TypeReference<List<Customer>>(){});

		Assert.isTrue(myObjects.size()>0, "Test failed while reading all customer");
	}


}
