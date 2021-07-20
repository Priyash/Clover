package com.clover.data.controller;

import com.clover.data.model.Product;
import com.clover.data.service.KafkaClientService;
import com.clover.data.service.MongoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
public class SchedulerController {

    @Autowired
    private MongoService mongoService;

    @Autowired
    private KafkaClientService kafkaClientService;

    @RequestMapping(value = "/schedule", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Scheduled(cron = "")
    public ResponseEntity<List<Product>> streamProductData() {
        List<Product> products = mongoService.fetchActiveProducts();
        for(Product product : products) {
            kafkaClientService.send(product);
        }
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }
}
