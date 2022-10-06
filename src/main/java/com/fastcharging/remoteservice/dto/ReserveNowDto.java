package com.fastcharging.remoteservice.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveNowDto {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private String chargeboxid;
	private String idTag;
	private int connectorId;
	private String expiryDate;

	// @Transient
	// private Date expiration;

	public Date getExpiration() throws ParseException {
		return dateFormat.parse(this.expiryDate);
	}

}
