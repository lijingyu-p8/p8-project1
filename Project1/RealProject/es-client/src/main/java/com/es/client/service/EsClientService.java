package com.es.client.service;

import java.io.IOException;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EsClientService {
	@Autowired
	private RestHighLevelClient restHighLevelClient;

	public CreateIndexResponse createIndex() {
		CreateIndexRequest createIndexRequest = null;
		RequestOptions options = RequestOptions.DEFAULT;
		CreateIndexResponse indexResponse = null;
		try {
			createIndexRequest = new CreateIndexRequest("api_test_index");
			createIndexRequest
					.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
			createIndexRequest.ma
			indexResponse = restHighLevelClient.indices().create(createIndexRequest, options);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indexResponse;
	}

}
