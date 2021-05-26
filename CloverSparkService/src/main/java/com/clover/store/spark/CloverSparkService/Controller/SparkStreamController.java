package com.clover.store.spark.CloverSparkService.Controller;

import com.clover.store.spark.CloverSparkService.Model.Product;
import com.clover.store.spark.CloverSparkService.Service.SparkStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class SparkStreamController {

    @Autowired
    private SparkStreamService sparkStreamService;

    @RequestMapping(value = "/fetchStream",  method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity receiveSparkKafkaStream(){
        sparkStreamService.startStream();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
