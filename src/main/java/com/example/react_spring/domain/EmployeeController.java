package com.example.react_spring.domain;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
public class EmployeeController {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/employee")
	public ArrayList<Employee> employee(@RequestParam(value = "lname", required = true) String lname,
			@RequestParam(value = "fname", required = false) String fname) {

		ArrayList<Employee> employeeList = new ArrayList<Employee>();

		if (fname == null) {
			System.out.println(String.format("Querying for customer records where last_name = '%s':", lname));
			jdbcTemplate
					.query("SELECT id, first_name, last_name FROM employee WHERE last_name = ?",
							(rs, rowNum) -> new Employee(rs.getString("first_name"),
									rs.getString("last_name")),
							new Object[] { lname })
					.forEach(employee -> employeeList.add(employee));
		} else {
			System.out.println(String.format("Querying for customer records where full name is: %s %s",
					new Object[] { fname, lname }));
			jdbcTemplate
					.query("SELECT id, first_name, last_name FROM employee WHERE first_name = ? and last_name = ?",
							(rs, rowNum) -> new Employee(rs.getString("first_name"),
									rs.getString("last_name")),
							new Object[] { fname, lname })
					.forEach(employee -> employeeList.add(employee));
		}

		return employeeList;
	}

	@GetMapping("/Allemployee")
	public ArrayList<Employee> employeeAll() {
		ArrayList<Employee> employeeList = new ArrayList<Employee>();
		jdbcTemplate
				.query("SELECT id, first_name, last_name FROM employee",
						(rs, rowNum) -> new Employee(rs.getString("first_name"),
								rs.getString("last_name")),
						new Object[] {})
				.forEach(employee -> employeeList.add(employee));
		return employeeList;
	}

	@PostMapping("/employee")
	@ResponseBody
	public ResponseEntity<?> newEmployee(@RequestBody NewEmployeeForm newEmployeeForm) {
		
		if (newEmployeeForm.getFirstName().equals("Avtansh")) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		int rowAffected = jdbcTemplate.update("INSERT INTO employee (first_name, last_name) VALUES (? , ?)",
				new Object[] { newEmployeeForm.getFirstName(), newEmployeeForm.getLastName() });
		
		if (rowAffected == 1) {
			return new ResponseEntity<>("Updated", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
