package com.company.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.company.app.service.LambdaService;
import com.company.app.to.DataTo;

@RestController
public class MainController {
	
	private static Logger LOG = LoggerFactory.getLogger(MainController.class);
	private final LambdaService service;
	
	@Autowired
	public MainController(LambdaService service) {
		super();
		this.service = service;
	}

	@PostMapping("/actions/calculate")
	public ResponseEntity<?> create(@RequestBody DataTo dataTo) {
		LOG.info("Input DataTo: {}",dataTo);
		return new ResponseEntity<>(service.calc(dataTo),HttpStatus.OK);
	}
	
}
