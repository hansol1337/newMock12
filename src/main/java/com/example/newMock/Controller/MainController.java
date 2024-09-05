package com.example.newMock.Controller;

import java.util.Random;
import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.math.BigDecimal;

@RestController
public class MainController {

    private final Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    Random random = new Random();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances (@RequestBody RequestDTO requestDTO) {

        try {
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;
            String currency;
            String rqUID = requestDTO.getRqUID();

            if (firstDigit == '8') {
                maxLimit = new BigDecimal("2000.00");
                currency = "US";
            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal("1000.00");
                currency = "EU";
            } else {
                maxLimit = new BigDecimal("10000.00");
                currency = "RUB";
            }
            Random random = new Random();
            BigDecimal balance = new BigDecimal(random.nextDouble()).multiply(maxLimit).setScale(2, BigDecimal.ROUND_HALF_UP);

            ResponseDTO responseDTO = new ResponseDTO();

            responseDTO.setRqUID(rqUID);
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currency);
            responseDTO.setBalance(balance);
            responseDTO.setMaxLimit(maxLimit);

            log.error("********** RequestDTO **********{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("********** ResponseDTO **********{}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
