//package com.sweet.utils;
//
//
//import com.sweet.common.Suggestion;
//import com.sweet.protocol.dto.PoemDto;
//import jakarta.annotation.Resource;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.client.elc.NativeQuery;
//import org.springframework.data.elasticsearch.core.IndexOperations;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
//import org.springframework.data.elasticsearch.core.query.Query;
//import org.springframework.data.elasticsearch.core.query.SearchTemplateQuery;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * es 工具类 poem
// * @author shuheng
// */
//@Component
//public class ElasticsearchUtil {
//    @Resource
//    private ElasticsearchTemplate restTemplate;
//
//    /**
//     * 判断索引是否存在索引
//     */
//    public <T> boolean existsIndex(Class<T> clazz) {
//        IndexOperations indexOperations = restTemplate.indexOps(clazz);
//        return indexOperations.exists();
//    }
//
//
//    /**
//     * 创建索引
//     */
//    public <T> void createIndex(Class<T> clazz) {
//        // spring data es所有索引操作都在这个接口
//        IndexOperations indexOperations = restTemplate.indexOps(clazz);
//        // 是否存在，存在则删除
//        if (indexOperations.exists()) {
//            indexOperations.delete();
//        }
//
//        // 创建索引
//        indexOperations.create();
//
//        //设置映射: 在正式开发中，几乎不会使用框架创建索引或设置映射，这是架构或者管理员的工作，不适合使用代码实现
//        restTemplate.indexOps(PoemDto.class).putMapping();
//    }
//
//    /**
//     * 删除索引
//     */
//    public <T> boolean deleteIndex(Class<T> clazz) {
//        IndexOperations indexOperations = restTemplate.indexOps(clazz);
//        return indexOperations.delete();
//    }
//
//    /**
//     * 新增文档
//     */
//    public <T> T insert(T entity) {
//        return restTemplate.save(entity);
//    }
//
//    /**
//     * 批量新增文档
//     */
//    public <T> Iterable<T> batchInsert(List<T> entities) {
//        return restTemplate.save(entities);
//    }
//
//    /**
//     * 根据id查询
//     */
//    public <T> T searchById(String id, Class<T> clazz) {
//        return restTemplate.get(id, clazz);
//    }
//
//    /**
//     * 根据id删除
//     */
//
//    public <T> String deleteById(String id, Class<T> clazz) {
//        return restTemplate.delete(id, clazz);
//    }
//
//    /**
//     * 查询所有
//     */
//    public <T> List<T> searchAll(Class<T> clazz) {
//        SearchHits<T> search = restTemplate.search(Query.findAll(), clazz);
//        List<SearchHit<T>> searchHits = search.getSearchHits();
//        // 获得searchHits,进行遍历得到content
//        List<T> entities = new ArrayList<>();
//        searchHits.forEach(hit -> {
//            entities.add(hit.getContent());
//        });
//        return entities;
//    }
//
//    /**
//     * match查询, 模糊查询 + 分页
//     */
//    public <T> List<T> matchQuery(String content, Integer page, Integer size, Class<T> clazz) {
//        // 创建查询
//        System.out.println(content);
//
//        Query query = NativeQuery.builder().withQuery(q -> q
//                .multiMatch(m ->
//                        m.fields("author","title", "content", "type", "dynasty", "form")
//                                .query(content)
//                                // 启用模糊匹配
//                                . fuzziness("AUTO"))
//        ).withPageable(Pageable.ofSize(size).withPage(page)).build();
//        return getData(query, clazz);
//    }
//
//    /**
//     * 分页查询
//     */
//    public <T> List<T> pageSearch(Integer page, Integer size, Class<T> clazz) {
//        Query query = NativeQuery.builder().withQuery(Query.findAll())
//                .withPageable(Pageable.ofSize(size).withPage(page)).build();
//
//        return getData(query, clazz);
//    }
//
//    /**
//     * 排序查询，根据id降序排列
//     */
//    public <T> List<T> sortSearch(String sort, Integer page, Integer size, Class<T> clazz) {
//        Query query = NativeQuery.builder().withQuery(Query.findAll())
//                .withPageable(Pageable.ofSize(size).withPage(page))
//                .withSort(Sort.by(sort).descending()).build();
//        return getData(query, clazz);
//    }
//
//
//    public <T> List<T> autocomplete(String prefix, Integer page, Integer size, Class<T> clazz) {
//
//        // 创建查询
//        Query query = NativeQuery.builder()
//                .withQuery(q -> q
//                        .bool(b -> b
//                                .must(m -> m
//                                        .matchPhrase(mq -> mq
//                                                .field("suggest")
//                                                .query(prefix)
//                                        )
//                                )
//                        )
//                )
//                .withPageable(Pageable.ofSize(size).withPage(page))
//                .build();
//
//        return getData(query, clazz);
//    }
//
//
//    public <T> List<T> getData(Query query, Class<T> clazz) {
//        SearchHits<T> searchHits = restTemplate.search(query, clazz);
//        // 获得searchHits,进行遍历得到content
//        List<T> entities = new ArrayList<>();
//        searchHits.forEach(hit -> {
//            entities.add(hit.getContent());
//        });
//        System.out.println(entities.size());
//        return entities;
//    }
//}
