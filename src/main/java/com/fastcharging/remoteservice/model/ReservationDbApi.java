package com.fastcharging.remoteservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDbApi {
    @JsonProperty("reservationPk")
    private int reservationPk;

    @JsonProperty("connectorPk")
    private int connectorPk;

    @JsonProperty("chargeBoxId")
    private String chargeBoxId;

    @JsonProperty("idTag")
    private String idTag;

    @JsonProperty("start_datetime")
    private String start_datetime;

    @JsonProperty("expiry_datetime")
    private String expiry_datetime;

    @JsonProperty("status")
    private String status;

    @JsonProperty("connectorId")
    private int connectorId;

    @JsonProperty("NameCS")
    private String name;

    @JsonProperty("location")
    private String location;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("City")
    private String city;
}