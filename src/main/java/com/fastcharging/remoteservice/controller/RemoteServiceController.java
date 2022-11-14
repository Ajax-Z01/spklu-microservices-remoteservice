package com.fastcharging.remoteservice.controller;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import com.fastcharging.remoteservice.client.UserClient;
import com.fastcharging.remoteservice.dto.CancelReservationDto;
import com.fastcharging.remoteservice.dto.RemoteStartTransactionEnhancedDto;
import com.fastcharging.remoteservice.dto.ReserveNowDto;
import com.fastcharging.remoteservice.dto.UserRestConsumer;
import com.fastcharging.remoteservice.model.ConnectorStatusDistanceDbApi;
import com.fastcharging.remoteservice.util.HaversineDistanceCalculator;

@RestController
@RequestMapping("api/action/command")
@CrossOrigin(origins = { "*" })
@Slf4j
public class RemoteServiceController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserClient userClient;

    @Value("${steve.baseurl}")
    String csmsUrl;

    @Value("${dbapi.baseurl}")
    String dbapi_baseurl;

    @Value("${dbapi.auth-param}")
    String dbapi_auth_param;

    @Value("${dbapi.auth-value}")
    String dbapi_auth_value;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(value = "/RemoteStartTransactionEnhanced/{kwhMobil}", method = RequestMethod.POST)
    public ResponseEntity<?> remoteStartTransactionEnhancedDto(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody RemoteStartTransactionEnhancedDto remoteStartTransactionEnhancedDto,
            @PathVariable("kwhMobil") Double kwhMobil) throws Exception {

        UserRestConsumer user = userClient.getUserDetail(headers);
        if (!user.getStatus().toString().equalsIgnoreCase("success")) {
            throw new Exception("Data pengguna tidak ditemukan.");
        }

        double latitudeCS = remoteStartTransactionEnhancedDto.getLatitudeCS();
        double longitudeCS = remoteStartTransactionEnhancedDto.getLongitudeCS();

        double latitudeMe = remoteStartTransactionEnhancedDto.getLatitudeMe();
        double longitudeMe = remoteStartTransactionEnhancedDto.getLongitudeMe();

        HttpHeaders headerFirst = new HttpHeaders();
        headerFirst.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headerFirst.set(dbapi_auth_param, dbapi_auth_value);
        HttpEntity<String> entity = new HttpEntity<String>(headerFirst);

        try {

            double distance = HaversineDistanceCalculator.haversine(latitudeCS, longitudeCS, latitudeMe, longitudeMe);

            boolean ok = (distance * 1000 <= 200);

            String permitted = ok ? "Permitted to charge" : "Not permitted to charge";

            if (ok) {
                HttpHeaders headers2 = new HttpHeaders();
                headers2.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                map.add("chargeboxid", remoteStartTransactionEnhancedDto.getChargeboxid());
                map.add("idTag", remoteStartTransactionEnhancedDto.getIdTag());
                map.add("connectorId", remoteStartTransactionEnhancedDto.getConnectorId());

                // HttpEntity<MultiValueMap<String, Object>> postEntity = new HttpEntity<>(map,
                // headers);

                // ResponseEntity<String> response = restTemplate.exchange(csmsUrl +
                // "RemoteStartTransaction",
                // HttpMethod.POST, postEntity, String.class);

                return new ResponseEntity<Object>(map, HttpStatus.OK);

            } else {
                HashMap<String, Object> mapSuccess = new HashMap<>();
                mapSuccess.put("status", "success");
                mapSuccess.put("distanceToCS", String.valueOf(distance));
                mapSuccess.put("permitted", permitted);
                mapSuccess.put("message", "List status connector complete ");

                return new ResponseEntity<Object>(mapSuccess, HttpStatus.OK);
            }

        } catch (RestClientException e) {
            HashMap<String, String> mapError = new HashMap<>();
            mapError.put("status", "error");
            mapError.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapError);
        }
    }

    @RequestMapping(value = "/RemoteStopTransactionEnhanced", method = RequestMethod.POST)
    public ResponseEntity<?> remoteStopTransactionEnhanced() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set(dbapi_auth_param, dbapi_auth_value);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        String token = bearerToken.substring(7);

        UserRestConsumer user = userClient.getUserDetail(headers);
        if (!user.getStatus().toString().equalsIgnoreCase("success")) {
            throw new Exception("Data pengguna tidak ditemukan.");
        }

        String idTag = user.getData().getTag_id();

        // URL conn_remote_start_url;
        return new ResponseEntity<Object>(idTag, HttpStatus.OK);
        // try {
        // conn_remote_start_url = new URL(dbapi_baseurl +
        // "transaction/currentRemoteStart/" + idTag);

        // String c_status = restTemplate.exchange(conn_remote_start_url.toURI(),
        // HttpMethod.GET, entity, String.class)
        // .getBody();

        // JsonNode jsonNodeCS = objectMapper.readTree(c_status);
        // String data_txt = jsonNodeCS.get("data").toString();

        // TransactionStartDbApi[] conn_remote_start = objectMapper.readValue(data_txt,
        // TransactionStartDbApi[].class);

        // if (conn_remote_start.length == 1) {
        // int currentTransaction = conn_remote_start[0].getTransactionPk();
        // String chargeBoxId = conn_remote_start[0].getChargeBoxId();

        // HttpHeaders headers2 = new HttpHeaders();
        // headers2.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        // map.add("chargeboxid", chargeBoxId);
        // map.add("transactionid", currentTransaction);

        // HttpEntity<MultiValueMap<String, Object>> postEntity = new HttpEntity<>(map,
        // headers2);

        // ResponseEntity<String> response = restTemplate.exchange(csmsUrl +
        // "RemoteStopTransaction",
        // HttpMethod.POST, postEntity, String.class);

        // return response;
        // } else {
        // HashMap<String, Object> mapSuccess = new HashMap<>();
        // mapSuccess.put("status", "success");
        // mapSuccess.put("message", "Tidak ditemukan transaksi untuk ID Tag ini");

        // return new ResponseEntity<Object>(mapSuccess, HttpStatus.OK);
        // }

        // } catch (MalformedURLException | RestClientException | URISyntaxException |
        // JsonProcessingException e) {
        // HashMap<String, String> mapError = new HashMap<>();
        // mapError.put("status", "error");
        // mapError.put("message", e.getMessage());

        // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapError);
        // }
    }

    @RequestMapping(value = "/ReserveNowTransaction", method = RequestMethod.POST)
    public ResponseEntity<?> reserveNowTransaction(@Valid @RequestBody ReserveNowDto reserveNowDto)
            throws ParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // headers.set(dbapi_auth_param, dbapi_auth_value);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = dateFormat.format(reserveNowDto.getExpiration());

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("chargeboxid", reserveNowDto.getChargeboxid());
        map.add("idTag", reserveNowDto.getIdTag());
        map.add("connectorId", reserveNowDto.getConnectorId());
        map.add("expiryDate", strDate);

        HttpEntity<MultiValueMap<String, Object>> postEntity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(csmsUrl + "ReserveNowTransaction", HttpMethod.POST,
                postEntity, String.class);

        // ResponseEntity<String> response = restTemplate.exchange(dbapi_baseurl +
        // "csms/ReserveNow", HttpMethod.POST,
        // postEntity, String.class);

        return response;
    }

    @RequestMapping(value = "/CancelReservationTransaction", method = RequestMethod.POST)
    public ResponseEntity<?> cancelReservationTransaction(
            @Valid @RequestBody CancelReservationDto cancelReservationDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // headers.set(dbapi_auth_param, dbapi_auth_value);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("chargeboxid", cancelReservationDto.getChargeboxid());
        map.add("reservationId", cancelReservationDto.getReservationId());

        HttpEntity<MultiValueMap<String, Object>> postEntity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(csmsUrl + "CancelReservationTransaction",
                HttpMethod.POST, postEntity, String.class);

        // ResponseEntity<String> response = restTemplate.exchange(dbapi_baseurl +
        // "csms/CancelReservation",
        // HttpMethod.POST, postEntity, String.class);

        return response;
    }
}
