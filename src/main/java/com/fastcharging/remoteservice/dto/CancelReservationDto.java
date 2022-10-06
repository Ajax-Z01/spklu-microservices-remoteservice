package com.fastcharging.remoteservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CancelReservationDto {
	private String chargeboxid;
	private int reservationId;
}
