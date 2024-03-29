package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins= "*")
@RestController
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;
	
	@CrossOrigin(origins = "*")
	@PostMapping("/customers")
	public Customers addCustomer(@RequestBody Customers customer, HttpSession session) {
		session.setAttribute("cust_email", customer.getEmail());
		return customerRepository.save(customer);
	}
	
	@SuppressWarnings("rawtypes")
	@CrossOrigin(origins = "*")
	@PostMapping("/customers/{email}")
	public boolean verifyLogin(@RequestBody Map loginData, @PathVariable(name = "email") String email, HttpSession session) {
		String lemail = (String) loginData.get("email");
		String lpassword = (String) loginData.get("password");
		Customers customer = customerRepository.findByEmail(email);
		if(customer!= null && customer.getEmail().equals(lemail) && customer.getPassword().equals(lpassword)) {
			session.setAttribute("cust_email", lemail);
			return true;
		}else {
			return false; 
		}
	}
	
	@GetMapping("/customers")
	public List<Customers> getAllCustomers(){
		return customerRepository.findAll();
	}
	
	@GetMapping("/customers/search/{keyword}")
	public List<Customers> searchCustomer(@PathVariable String keyword){
		return customerRepository.searchCustomer(keyword);
	}
	
	@DeleteMapping("/customers/{email}")
	public ResponseEntity<Map<String, Boolean>> deleteCustomer(@PathVariable String email){
		Customers customer = customerRepository.findByEmail(email);
		customerRepository.delete(customer);
		Map<String, Boolean> map = new HashMap<>();
		map.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/customers/{cust_email}")
	public Customers getCustomer(@PathVariable String cust_email) {
		return customerRepository.findByEmail(cust_email);
		
	}
}
