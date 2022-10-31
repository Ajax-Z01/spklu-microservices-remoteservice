package com.fastcharging.remoteservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConnectorStatusDistanceDbApi {

    @JsonProperty("connectorStatus")
    private ConnectorStatusImprovedDbApi connectorStatus;

    private int distance;
    private String distanceReadable;
    private int duration;
    private String durationReadable;
}