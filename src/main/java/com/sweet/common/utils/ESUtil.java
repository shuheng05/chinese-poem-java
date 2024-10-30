package com.sweet.common.utils;

import com.alibaba.fastjson.JSON;
import com.sweet.poem.protocol.dto.PoemDto;
import jakarta.annotation.Resource;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ES工具类
 * @author shuheng
 */
@Component
public class ESUtil {
    @Resource
    private RestHighLevelClient client;

    /**
     * 新增文档 一个
     */
    void addDocument(PoemDto poemDto) {
        IndexRequest request = new IndexRequest("poem");
        request.source(poemDto, XContentType.JSON);
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 新增文档 多个
     */
    void addDocuments(List<PoemDto> poemDtos) {
        BulkRequest request = new BulkRequest();
        for (PoemDto poemDto : poemDtos) {
            request.add(new IndexRequest("poem").
                    id(poemDto.getId().toString()).
                    source(JSON.toJSONString(poemDto), XContentType.JSON));
        }
        try {
            client.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询文档 id
     */
    PoemDto getDocumentById(String id) throws IOException {
        GetRequest request = new GetRequest("poem", id);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String poemJson = response.getSourceAsString();
        return JSON.parseObject(poemJson, PoemDto.class);
    }

    /**
     * 删除文档 id
     */
    void deleteDocumentById(String id) throws IOException {
        DeleteRequest request = new DeleteRequest("poem", id);
        client.delete(request, RequestOptions.DEFAULT);
    }

    /**
     * 查询所有数据
     */
    List<PoemDto> queryAll() throws IOException {
        SearchRequest request = new SearchRequest("poem");
        request.source().query(QueryBuilders.matchAllQuery());
        return getPoemDtos(request);
    }

    /**
     * 查询 match 等 分页 排序高亮
     */
    List<PoemDto> matchQuery(Integer start, Integer count) throws IOException {
        SearchRequest request = new SearchRequest("poem");
        request.source().query(QueryBuilders.matchQuery("all", "唐"));
        //request.source().query(QueryBuilders.multiMatchQuery("唐", "dynasty", "title"));
        //request.source().query(QueryBuilders.termQuery("id", "1"));
        request.source().from(start).size(count);
        //request.source().sort("id", SortOrder.DESC);
        request.source().highlighter(new HighlightBuilder()
                .field("title")
                .field("author")
                .field("content")
                .requireFieldMatch(false));


        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析结果
        SearchHits hits = response.getHits();
        // 获取高亮
        //long total = hits.getTotalHits().value;
        //System.out.println(total);
        SearchHit[] poems = hits.getHits();
        List<PoemDto> poemDtos = new ArrayList<>();
        for (SearchHit poem : poems) {
            String poemString = poem.getSourceAsString();
            PoemDto poemDto = JSON.parseObject(poemString, PoemDto.class);
            // 获取高亮
            Map<String, HighlightField> highlightFields = poem.getHighlightFields();
            if (!(highlightFields == null || highlightFields.isEmpty())) {
                HighlightField highlightFieldTitle = highlightFields.get("title");
                HighlightField highlightFieldAuthor = highlightFields.get("author");
                HighlightField highlightFieldContent = highlightFields.get("content");
                if (highlightFieldTitle != null) {
                    String title = highlightFieldTitle.getFragments()[0].string();
                    poemDto.setTitle(title);
                }
                if (highlightFieldAuthor != null) {
                    String author = highlightFieldAuthor.getFragments()[0].string();
                    poemDto.setAuthor(author);
                }
                if (highlightFieldContent != null) {
                    String content = highlightFieldContent.getFragments()[0].string();
                    poemDto.setContent(content);
                }
            }
        }

        return poemDtos;
    }

    /**
     * 多条件查询
     */
    List<PoemDto> boolQueryAll() throws IOException {
        SearchRequest request = new SearchRequest("poem");

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        boolQuery.must(QueryBuilders.termQuery("dynasty", "唐"));

        request.source().query(boolQuery);
        return getPoemDtos(request);
    }

    public List<PoemDto> getPoemDtos(SearchRequest request) throws IOException {
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析结果
        SearchHits hits = response.getHits();
        SearchHit[] poems = hits.getHits();
        List<PoemDto> poemDtos = new ArrayList<>();
        for (SearchHit poem : poems) {
            String poemString = poem.getSourceAsString();
            poemDtos.add(JSON.parseObject(poemString, PoemDto.class));
        }
        return poemDtos;
    }
}
