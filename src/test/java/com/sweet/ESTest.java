package com.sweet;

import com.alibaba.fastjson.JSON;
import com.sweet.poem.mapper.PoemMapper;
import com.sweet.poem.protocol.dto.PoemDto;
import com.sweet.poem.protocol.query.PoemsQueryDto;
import jakarta.annotation.Resource;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

@SpringBootTest
public class ESTest {
    private RestHighLevelClient client;

    private final String MAPPING_TEMPLATE =
            """
                    {
                      "mappings": {
                        "properties": {
                          "id": {
                            "type": "keyword"
                          },
                          "title": {
                            "type": "text",
                            "analyzer": "ik_max_word",
                            "copy_to": "all"
                          },
                          "content": {
                            "type": "text",
                            "analyzer": "ik_max_word",
                             "copy_to": "all"
                          },
                          "author": {
                            "type": "text",
                            "analyzer": "ik_max_word",
                             "copy_to": "all"
                          },
                          "dynasty": {
                            "type": "text",
                            "analyzer": "ik_max_word",
                             "copy_to": "all"
                          },
                          "annotation": {
                            "type": "text",
                            "analyzer": "ik_max_word",
                            "index": false\s
                          },
                          "translation": {
                            "type": "text",
                            "analyzer": "ik_max_word",
                            "index": false\s
                          },
                          "appreciate": {
                            "type": "text",
                            "analyzer": "ik_max_word",
                            "index": false\s
                          },
                          "mp4Url": {
                            "type": "keyword",
                            "index": false\s
                          },
                          "form": {
                            "type": "text",
                            "analyzer": "ik_smart",
                            "copy_to": "all"
                          },
                          "type": {
                            "type": "text",
                            "analyzer": "ik_smart",
                            "copy_to": "all"
                          },
                          "all": {
                            "type": "text",
                            "analyzer": "ik_max_word"
                          }
                        }
                      }
                    }
                    """;

    @Test
    void test() {
        System.out.println(client);
    }

    /**
     * 创建索引
     */
    @Test
    void createIndex() throws IOException {
        //创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("poem");

        //配置索引参数, DSL语句
        request.source(MAPPING_TEMPLATE, XContentType.JSON);

        //执行请求
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        //响应状态
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    /**
     * 删除索引
     */
    @Test
    void testDeleteIndex() throws IOException {
        //删除索引请求
        DeleteIndexRequest request = new DeleteIndexRequest("poem");

        boolean acknowledged = client.indices().delete(request, RequestOptions.DEFAULT).isAcknowledged();
        System.out.println(acknowledged);
    }

    /**
     * 判断索引是否存在
     */
    @Test
    void testSearch() throws IOException {
        GetIndexRequest request = new GetIndexRequest("poem");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Resource
    private PoemMapper poemMapper;

    /**
     * 新增文档 单个
     */
    @Test
    void testIndexDocument() throws IOException {
        PoemDto poemDto = new PoemDto();
        IndexRequest request = new IndexRequest("poem");
        request.source(poemDto, XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 新增文档 多个
     */
    @Test
    void testIndexDocuments() throws IOException {
        PoemsQueryDto poemsQueryDto = new PoemsQueryDto(0, 21, null, null, null, null);
        List<PoemDto> poemDtos = poemMapper.selectPage(poemsQueryDto);
        for (PoemDto poemDto : poemDtos) {
            List<String> suggestion = new ArrayList<>();

            String type = poemDto.getType();
            String[] types = type.split(",");
            Collections.addAll(suggestion, types);
            suggestion.add(poemDto.getTitle());
            suggestion.add(poemDto.getContent());
            suggestion.add(poemDto.getAuthor());
            suggestion.add(poemDto.getDynasty());
            suggestion.add(poemDto.getForm());
            poemDto.setSuggestion(suggestion);
            System.out.println(suggestion);
        }
        BulkRequest request = new BulkRequest();
        for (PoemDto poemDto : poemDtos) {
            request.add(new IndexRequest("poem").
                    id(poemDto.getId().toString()).
                    source(JSON.toJSONString(poemDto), XContentType.JSON));
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * 查询文档 id
     */
    @Test
    void testBGetDocumentById() throws IOException {
        GetRequest request = new GetRequest("poem", "1");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String poemJson = response.getSourceAsString();
        PoemDto poemDto = JSON.parseObject(poemJson, PoemDto.class);
        System.out.println(poemDto);
    }

    /**
     * 删除文档 id
     */
    @Test
    void testDeleteDocumentById() throws IOException {
        for (int i = 1; i <= 21; i++) {
            DeleteRequest request = new DeleteRequest("poem", String.valueOf(i));
            client.delete(request, RequestOptions.DEFAULT);
        }

    }

    /**
     * 查询所有数据
     */
    @Test
    void testQueryAll() throws IOException {
        SearchRequest request = new SearchRequest("poem");
        request.source().query(QueryBuilders.matchAllQuery());

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析结果
        SearchHits hits = response.getHits();

        SearchHit[] poems = hits.getHits();
        System.out.println("count: " + poems.length);
        for (SearchHit poem : poems) {
            String poemString = poem.getSourceAsString();
            PoemDto poemDto = JSON.parseObject(poemString, PoemDto.class);
            System.out.println(poemDto);
        }
    }

    /**
     * 查询用户推荐
     */
    @Test
    void testQueryScore() throws IOException {
        SearchRequest request = new SearchRequest("poem");

        FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(
                // 基础查询，这里使用match all query表示匹配所有文档
                QueryBuilders.matchAllQuery(),

                // 定义多个评分函数及其对应的过滤条件
                new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                        // 为特定作者的诗歌添加权重
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("author", "李白"), ScoreFunctionBuilders.weightFactorFunction(0.3f)),
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("author", "王维"), ScoreFunctionBuilders.weightFactorFunction(0.2f)),
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("author", "杜甫"), ScoreFunctionBuilders.weightFactorFunction(0.1f)),

                        // 为特定类型的诗歌添加权重
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("type", "怀古思乡"), ScoreFunctionBuilders.weightFactorFunction(0.15f)),
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("type", "家国情怀"), ScoreFunctionBuilders.weightFactorFunction(0.1f)),
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("type", "抒情"), ScoreFunctionBuilders.weightFactorFunction(0.05f)),

                        // 为特定形式的诗歌添加权重
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("form", "诗"), ScoreFunctionBuilders.weightFactorFunction(0.05f)),
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("form", "词"), ScoreFunctionBuilders.weightFactorFunction(0.03f)),
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.termQuery("form", "赋"), ScoreFunctionBuilders.weightFactorFunction(0.02f)),

                }
        );
        // 设置得分模式为sum，即将所有函数得分相加
        functionScoreQuery.scoreMode(FunctionScoreQuery.ScoreMode.SUM);
        // 设置提升模式为sum，即将基础得分与函数得分相加
        functionScoreQuery.boostMode(CombineFunction.SUM);


        request.source().query(functionScoreQuery);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析结果
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits().value;
        System.out.println(total);
        SearchHit[] poems = hits.getHits();
        for (SearchHit poem : poems) {
            String poemString = poem.getSourceAsString();
            PoemDto poemDto = JSON.parseObject(poemString, PoemDto.class);
            System.out.println(poemDto);
        }




    }

    /**
     * 查询 match 等 分页 排序高亮
     */
    @Test
    void testMatchQueryAll() throws IOException {
        SearchRequest request = new SearchRequest("poem");
        request.source().query(QueryBuilders.matchQuery("all", "唐"));
        //request.source().query(QueryBuilders.multiMatchQuery("唐", "dynasty", "title"));
        //request.source().query(QueryBuilders.termQuery("id", "1"));
        //request.source().from(0).size(10);
        //request.source().sort("id", SortOrder.DESC);
        request.source().highlighter(new HighlightBuilder()
                .field("dynasty")
                .requireFieldMatch(false));


        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析结果
        SearchHits hits = response.getHits();

        // 获取高亮

        long total = hits.getTotalHits().value;
        System.out.println(total);
        SearchHit[] poems = hits.getHits();
        for (SearchHit poem : poems) {
            String poemString = poem.getSourceAsString();
            PoemDto poemDto = JSON.parseObject(poemString, PoemDto.class);
            // 获取高亮
            Map<String, HighlightField> highlightFields = poem.getHighlightFields();
            if (!(highlightFields == null || highlightFields.isEmpty())) {
                HighlightField highlightField = highlightFields.get("dynasty");
                if (highlightField != null) {
                    String dynasty = highlightField.getFragments()[0].string();
                    poemDto.setDynasty(dynasty);
                }
            }
            System.out.println(poemDto);
        }
    }

    /**
     * 多条件查询
     */
    @Test
    void testBoolQueryAll() throws IOException {
        SearchRequest request = new SearchRequest("poem");

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        boolQuery.must(QueryBuilders.termQuery("dynasty", "唐"));

        request.source().query(boolQuery);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        // 解析结果
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits().value;
        System.out.println(total);
        SearchHit[] poems = hits.getHits();
        for (SearchHit poem : poems) {
            String poemString = poem.getSourceAsString();
            PoemDto poemDto = JSON.parseObject(poemString, PoemDto.class);
            System.out.println(poemDto);
        }
    }

    /**
     * 自动补全
     */
    @Test
    void testSuggest() throws IOException {
        SearchRequest request = new SearchRequest("poem");
        request.source().suggest(new SuggestBuilder().addSuggestion(
                "mySuggestion",
                SuggestBuilders.completionSuggestion("suggestion")
                        .prefix("李")
                        .skipDuplicates(true)
                        .size(10)
        ));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        Suggest suggest = response.getSuggest();
        CompletionSuggestion completionSuggestion = suggest.getSuggestion("mySuggestion");
        for (CompletionSuggestion.Entry.Option option : completionSuggestion.getOptions()) {
            String sugggestion = option.getText().string();
            System.out.println(sugggestion);
        }
    }


    @BeforeEach
    void setClient() {
        this.client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://119.29.215.213:9200")));
    }

    @AfterEach
    void close() throws IOException {
        this.client.close();
    }
}
