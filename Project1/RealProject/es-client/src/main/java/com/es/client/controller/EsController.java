package com.es.client.controller;

import org.elasticsearch.client.indices.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.es.client.service.EsClientService;

@RestController
public class EsController {
	@Autowired
	private EsClientService esClientService;

	@GetMapping("createIndex")
	public CreateIndexResponse createIndex() {
		return esClientService.createIndex();
	}
}
