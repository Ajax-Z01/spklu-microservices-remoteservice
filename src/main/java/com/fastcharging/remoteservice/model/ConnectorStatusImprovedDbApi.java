package com.fastcharging.remoteservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConnectorStatusImprovedDbApi {
    private String id;

    private int chargeBoxPk;

    private String chargeBoxId;

    private int connectorPk;

    @JsonProperty("connectorId")
    private int connectorId;

    private String plugType;

    @JsonProperty("max_status_timestamp")
    private String max_status_timestamp;

    @JsonProperty("name")
    private String name;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("last_heartbeat_timestamp_gmt7")
    private String last_heartbeat_timestamp_gmt7;

    @JsonProperty("coordinate")
    private String coordinate;
}