package com.fastcharging.remoteservice.dto;

import lombok.Data;

@Data
public class RemoteStartTransactionEnhancedDto {
	private String chargeboxid;
	private String idTag;
	private int connectorId;
	private double latitudeCS;
	private double longitudeCS;
	private double latitudeMe;
	private double longitudeMe;
}
