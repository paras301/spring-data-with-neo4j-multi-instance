package com.company.service;

import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.vo.CypherQueries;
import com.company.vo.OrderCartInput;
import com.company.vo.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderProcessingService {

	private final Driver driver;

	public OrderProcessingService(Driver driver) {
		this.driver = driver;
	}

	@Autowired
	CypherQueries cypherQueries;

	public String processOrder(OrderCartInput req) throws Exception {
		log.info("req --> " + req);

		String createOrder = mapStringToMapping(cypherQueries.getCypher_queries().get("createOrder"), req);

		for (OrderItem item : req.getItems()) {
			String createOrder1 = mapStringToMapping(createOrder, item);

			try (Session session = driver.session(); Transaction tx = session.beginTransaction()) {
				tx.run(createOrder1);
				tx.commit();
			} catch (Exception e) {
				log.error("Exception while processing data to Neo4j... ", e);
			}

		}

		return "Customer Order Processed Successfully";

	}

	private String mapStringToMapping(String query, Object mapping) {
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> query_params = oMapper.convertValue(mapping, Map.class);

		for (Map.Entry<String, Object> entry : query_params.entrySet()) {
			if (query.contains("${" + entry.getKey() + "}"))
				query = query.replace("${" + entry.getKey() + "}", (String) entry.getValue());
		}

		log.info("Executing cypher query --> " + query);

		return query;
	}
}
