package com.clover.elasticsearch.service;

import com.clover.elasticsearch.config.ElasticSearchYMLConfig;
import com.clover.elasticsearch.model.Product;
import com.clover.elasticsearch.util.IndexMapUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.PlainActionFuture;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class IndexingServiceImpl implements IndexingService{

    @Autowired
    private ElasticSearchYMLConfig ymlConfig;

    @Autowired
    RestHighLevelClient client;

    @Autowired
    private IndexMapUtil mapUtil;

    @Override
    public void bulkIndexProductData(List<Product> products) {
        log.info("Bulk Indexing started...");
        log.info("Products data of list: {} of size: {}",String.valueOf(products.toArray().toString()), products.size());
        if(!ObjectUtils.isEmpty(products)) {
            BulkRequest bulkRequest = new BulkRequest();
            products.forEach(product -> {
                try {
                    XContentBuilder builder = XContentFactory.jsonBuilder();
                    builder.startObject();
                    {
                        builder.field("b", product.getB());
                    }
                    builder.endObject();
                    IndexRequest indexRequest = new IndexRequest(ymlConfig.getIndex().getIndexName())
                            .id(String.valueOf(product.getA()))
                            .source(builder);
                    log.info("Indexing Request: {}", indexRequest.toString());
                    log.info("Indexing Request ID: {}", indexRequest.id());
                    bulkRequest.add(indexRequest);
                } catch (IOException e) {
                    log.error("IOException while adding indexing request to bulkRequest of ID: {}", product.getA());
                } catch (Exception ex) {
                    log.error("Exception while adding indexing request to bulkRequest of ID: {}", product.getA());
                }
            });

            bulkRequest.timeout(TimeValue.timeValueMillis(10000));
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
            bulkRequest.waitForActiveShards(ActiveShardCount.ALL);

            try {
                client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
                    @Override
                    public void onResponse(BulkResponse bulkResponse) {
                        for (BulkItemResponse bulkItemResponse : bulkResponse) {
                            DocWriteResponse itemResponse = bulkItemResponse.getResponse();
                            IndexResponse indexResponse = (IndexResponse) itemResponse;

                            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                                log.info("****************** ElasticSearch document has been created ****************");
                                log.info("ElasticSearch document creation status: {}", indexResponse.status());
                                log.info("Document has been stored to elasticsearch with index: {}, id: {}", indexResponse.getIndex(), indexResponse.getId());
                            }

                            if (bulkItemResponse.isFailed()) {
                                BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                                System.out.println("Bulk response has failed : " + failure.getMessage() + " with ID : " + failure.getId() + " of index " + failure.getIndex());
                                log.error("FAILURE!!! while storing documents using bulkAsync API");
                                log.error("Failure status: {}, message: {}, index: {}, id: {}", failure.getStatus(), failure.getMessage(), failure.getId(), failure.getIndex());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        log.error("FAILURE!!! while indexing bulk request to elasticsearch ", e);
                    }
                });

            } catch (ElasticsearchException esx) {
                if (esx.status() == RestStatus.CONFLICT) {
                    log.error("ElasticsearchException while indexing bulk request to elasticsearch with index: {}, status: {}", esx.getIndex(), esx.status(), esx);
                }
            } catch (Exception ex) {
                log.error("Exception while indexing bulk request to elasticsearch ", ex);
            }
        }
    }
}
