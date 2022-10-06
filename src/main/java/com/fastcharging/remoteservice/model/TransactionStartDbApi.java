package com.fastcharging.remoteservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionStartDbApi {

	@JsonProperty("transaction_pk")
	// @JsonProperty("transactionPk")
	private int transactionPk;

	@JsonProperty("charge_box_id")
	// @JsonProperty("chargeBoxId")
	private String chargeBoxId;

	@JsonProperty("connector_id")
	// @JsonProperty("connectorId")
	private int connectorId;

	@JsonProperty("start_timestamp")
	// @JsonProperty("startTimestamp")
	private String startTimestamp;

	@JsonProperty("start_value")
	// @JsonProperty("startValue")
	private String startValue;

	@JsonProperty("nameCS")
	private String nameCS;

	@JsonProperty("address")
	private String address;

	@JsonProperty("latitude")
	private String latitude;

	@JsonProperty("longitude")
	private String longitude;
}
