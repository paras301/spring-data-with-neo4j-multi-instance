package com.company.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.application.Neo4jConfiguration;
import com.company.vo.CypherQueries;
import com.company.vo.OrderCartInput;
import com.company.vo.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderProcessingService {

	@Autowired
    Neo4jConfiguration neo4jConfiguration;

	@Autowired
	CypherQueries cypherQueries;
	

	public String processOrder(OrderCartInput req) throws Exception {
		log.info("req --> " + req);

		String createOrderQuery = mapStringToMapping(cypherQueries.getCypher_queries().get("createOrder"), req);
		createOrderQuery = createOrderQuery.replace("${name}", getCustomerName(req));

		for (OrderItem item : req.getItems()) {
			String createOrderQuery1 = mapStringToMapping(createOrderQuery, item);

			try (Session session = neo4jConfiguration.neo4j2Driver().session(SessionConfig.forDatabase(neo4jConfiguration.getNeo4j2_database())); 
				Transaction tx = session.beginTransaction()) {
				tx.run(createOrderQuery1);
				tx.commit();
			} catch (Exception e) {
				log.error("Exception while processing data to Neo4j... ", e);
			}
		}

		return "Customer Order Processed Successfully";

	}
	
	private String getCustomerName(OrderCartInput req) throws Exception {
		String getCustomerMasterQuery = mapStringToMapping(cypherQueries.getCypher_queries().get("getCustomerMaster"), req);
		
		try (Session session = neo4jConfiguration.neo4j1Driver().session(SessionConfig.forDatabase(neo4jConfiguration.getNeo4j1_database()));
				Transaction tx = session.beginTransaction()) {
				Result r = tx.run(getCustomerMasterQuery);
				
				List<String> result = r.list().stream().map(temp -> temp.asMap().get("c.name").toString()).collect(Collectors.toList());
				
				if(result.size() != 1) {
					log.error("No customer found or more than 1 customer found for same customer id");
				}	
				else {
					log.info("Customer name --> " + result.get(0));
					return result.get(0);
				}
				
			} catch (Exception e) {
				log.error("Exception while processing data to Neo4j... ", e);
			}
		return null;
		
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
