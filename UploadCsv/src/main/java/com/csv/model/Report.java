package com.csv.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="reports")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Report {

	@Id
	@Column(name = "batch_id")
	private long batchId;
	@Column(name="customer_id")
	private long customerId;
	@Column(name="account_id")
	private long accountId;
	@Column(name="transaction_type")
	private String transactionType;
	@Column(name="transaction_amount")
	private double transactionAmount;
	//private Date transaction_time;
}
