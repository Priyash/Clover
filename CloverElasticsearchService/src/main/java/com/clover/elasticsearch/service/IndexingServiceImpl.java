package com.clover.elasticsearch.service;

import com.clover.elasticsearch.config.ElasticSearchYMLConfig;
import com.clover.elasticsearch.model.Product;
import com.clover.elasticsearch.util.IndexMapUtil;
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
public class IndexingServiceImpl implements IndexingService{

    @Autowired
    private ElasticSearchYMLConfig ymlConfig;

    @Autowired
    RestHighLevelClient client;

    @Autowired
    private IndexMapUtil mapUtil;

    @Override
    public void bulkIndexProductData(List<Product> products) {
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
                    bulkRequest.add(indexRequest);
                } catch (IOException e) {
                    e.printStackTrace();
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
                                System.out.println("============= Doc has been created =========");
                                System.out.println("Document has been saved to elasticsearch with ID: " + indexResponse.getId());
                                System.out.println("=================================================");
                            }

                            if (bulkItemResponse.isFailed()) {
                                BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                                System.out.println("Bulk response has failed : " + failure.getMessage() + " with ID : " + failure.getId() + " of index " + failure.getIndex());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("Exception while indexing bulk request to elasticsearch : " + e.getMessage());
                    }
                });

            } catch (ElasticsearchException esx) {
                if (esx.status() == RestStatus.CONFLICT) {
                    esx.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
