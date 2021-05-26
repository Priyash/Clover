package com.clover.store.clover.controller;

import com.clover.store.clover.model.Product;
import com.clover.store.clover.service.MessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class MessageQueueController {

    @Autowired
    private MessageQueueService messageQueueService;

    @RequestMapping(value = "/publish", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> publishMessage(@RequestBody Product product) {
        messageQueueService.send(product);
        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/receive", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> receiveMessages(){
        List<Product> productList = messageQueueService.receive();
        return new ResponseEntity<List<Product>>(productList, HttpStatus.CREATED);
    }
}
