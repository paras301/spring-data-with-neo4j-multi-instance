package com.company.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderCartInput {
	private String customerId;
	private String name;
	private String orderId;
	private String orderDate;
	private List<OrderItem> items;
}
