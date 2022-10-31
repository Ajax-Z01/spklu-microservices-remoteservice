package com.fastcharging.remoteservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionMonitorDbApi {
    private String id;

    private int transactionPk;

    private int connectorPk;

    private String startValue;

    private String stopValue;

    private String startTimestamp;

    private String stopTimestamp;

    private int connectorId;

    private String CSName;

    private String connectorName;

    private String idTag;

    private String customerName;

    private String city;

    private int priceKwh;
}