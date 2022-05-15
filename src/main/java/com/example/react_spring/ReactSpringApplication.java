package com.example.react_spring;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class ReactSpringApplication

implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ReactSpringApplication.class, args);
	}
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Preparing database...");

		jdbcTemplate.execute("DROP TABLE employee IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE employee(" + "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

		// Split up the array of whole names into an array of first/last names
		List<Object[]> splitUpNames = Arrays.asList("Ivan Chan","Dagny Taggart", "Francisco d'Anconia", "John Galt", "Henry Rearden", "Eddie Willers", "Ragnar Danneskjöld").stream()
				.map(name -> name.split(" ")).collect(Collectors.toList());

		// Use a Java 8 stream to print out each tuple of the list
		splitUpNames.forEach(name -> System.out.println(String.format("Inserting employee record for %s %s", name[0], name[1])));

		// Uses JdbcTemplate's batchUpdate operation to bulk load data
		jdbcTemplate.batchUpdate("INSERT INTO employee(first_name, last_name) VALUES (?,?)", splitUpNames);

	}
	
}
