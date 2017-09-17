/**
 * 
 */
package com.customer.unittest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.customer.entity.CustomerEntity;
import com.customer.repository.CustomerRepository;
import com.customer.service.CustomerService;
import com.customer.service.CustomerServiceImpl;
import com.model.user.Customer;

/**
 * @author vkamble
 *
 */
@RunWith(SpringRunner.class)
public class CustomerServiceTest {

    @TestConfiguration
    static class CustomerServiceImplTestContextConfiguration {
  
        @Bean
        public CustomerService customerService() {
            return new CustomerServiceImpl();
        }
    }
    	
	@Autowired
	private CustomerService customerService;

	@MockBean
	CustomerRepository customerRepository;
	
	@Before
	public void setUp(){
		
		CustomerEntity customerEntity1 = new CustomerEntity();
		customerEntity1.setName("Vaibhav");
		customerEntity1.setEmailId("test@test.com");
		customerEntity1.setCreatedDate(new Date());
		customerEntity1.setUpdatedDate(new Date());

		CustomerEntity customerEntity2 = new CustomerEntity();
		customerEntity2.setName("Vaibhav");
		customerEntity2.setEmailId("test@test.com");
		customerEntity2.setCreatedDate(new Date());
		customerEntity2.setUpdatedDate(new Date());
		customerEntity2.setId(1);

		List<CustomerEntity> customerEntities= new ArrayList<CustomerEntity>();
		customerEntities.add(customerEntity2);

		Mockito.when(customerRepository.exists(customerEntity2.getId())).thenReturn(true);

		Mockito.when(customerRepository.findOne(customerEntity2.getId())).thenReturn(customerEntity2);
		
		Mockito.when(customerRepository.findAll()).thenReturn(customerEntities);
		
		Mockito.when(customerRepository.save(Mockito.any(CustomerEntity.class)))
        .thenAnswer(new Answer<CustomerEntity>() {
            @Override
            public CustomerEntity answer(InvocationOnMock invocation) throws Throwable {
            	CustomerEntity customerEntity = (CustomerEntity) invocation.getArguments()[0];
            	customerEntity.setId(1L);
                return customerEntity;
            }
        });		
	}
	
	

	@Test
	public void testSaveCustomer(){
		
		Customer customer = new Customer();
		customer.setName("Vaibhav");
		customer.setEmailId("test@test.com");
		customer.setCreatedDate(new Date());
		customer.setUpdatedDate(new Date());
		
		customer = customerService.saveCustomer(customer);
		Assert.notNull(customer,"Customer is null");
		Assert.isTrue(customer.getId()==1,"Customer is not stored");
	}
	
	@Test
	public void testdeleteCustomerWithExistingId(){
		
		boolean result = customerService.deleteCustomer(1L);
		Assert.isTrue(result==true,"Customer not found");
	}


	@Test
	public void testIsCustomerExistsWithValidId(){
		
		boolean result = customerService.isCustomerExists(1L);
		Assert.isTrue(result==true,"Customer not found");
	}
	
	@Test
	public void testIsCustomerExistsWithInValidId(){
		
		boolean result = customerService.isCustomerExists(2L);
		Assert.isTrue(result==false,"Customer found");
	}

	
	@Test
	public void testGetCustomerWithValidId(){
		
		Customer customer = customerService.getCustomer(1L);
		Assert.notNull(customer,"Customer is null");
		Assert.isTrue(customer.getId()==1,"Customer is null");
	}
	
	@Test
	public void testGetCustomerWithInValidId(){
		
		Customer customer = customerService.getCustomer(2L);
		Assert.isNull(customer,"Customer is not null");
	}

	@Test
	public void testGetAllCustomers(){
		
		List<Customer> customers = customerService.getAllCustomers();
		Assert.notNull(customers,"Customer list is null");
		Assert.isTrue(customers.size()>0,"Customer list is empty");
	}


}
