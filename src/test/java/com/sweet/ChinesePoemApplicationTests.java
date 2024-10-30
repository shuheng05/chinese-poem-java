//package com.sweet;
//
//import com.sweet.common.Suggestion;
//import com.sweet.mapper.PoemMapper;
//import com.sweet.protocol.dto.PoemDto;
//import com.sweet.protocol.query.PoemsQueryDto;
//import com.sweet.utils.ElasticsearchUtil;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.client.elc.NativeQuery;
//import org.springframework.data.elasticsearch.core.IndexOperations;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.Query;
//import org.springframework.data.elasticsearch.core.suggest.Completion;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
////创建测试方法
//@SpringBootTest
//class ChinesePoemApplicationTests {
//    @Autowired
//    private ElasticsearchTemplate restTemplate;
//
//    @Autowired
//    private ElasticsearchUtil elasticsearchUtil;
//
//    @Test
//    void autocomplete() {
//        System.out.println(elasticsearchUtil.existsIndex(Suggestion.class));
//        System.out.println(elasticsearchUtil.searchAll(Suggestion.class).size());
//    }
//
//
//    @Autowired
//    private PoemMapper poemMapper;
//
//    /**
//     * 判断索引是否存在索引
//     */
//    @Test
//    void existsIndex() {
//        IndexOperations indexOperations = restTemplate.indexOps(PoemDto.class);
//        boolean exists = indexOperations.exists();
//        System.out.println(exists);
//    }
//
//    /**
//     * 创建索引
//     */
//    @Test
//    void createIndex() {
//        // spring data es所有索引操作都在这个接口
//        IndexOperations indexOperations = restTemplate.indexOps(PoemDto.class);
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
//    @Test
//    void deleteIndex() {
//        IndexOperations indexOperations = restTemplate.indexOps(PoemDto.class);
//        boolean delete = indexOperations.delete();
//        System.out.println(delete);
//    }
//
//    /**
//     * 新增文档
//     */
//    @Test
//    void insert() {
//        PoemDto poemDto = new PoemDto();
//        PoemDto saved = restTemplate.save(poemDto);
//        System.out.println(saved);
//    }
//
//    /**
//     * 批量新增文档
//     */
//    @Test
//    void batchInsert() {
//        PoemsQueryDto poemsQueryDto = new PoemsQueryDto(0, 21, null, null, null, null);
//        List<PoemDto> poemDtos = poemMapper.selectPage(poemsQueryDto);
//        Iterable<PoemDto> result = restTemplate.save(poemDtos);
//        System.out.println(result);
//    }
//
//    /**
//     * 根据id查询
//     */
//    @Test
//    void searchById() {
//        PoemDto poemDto = restTemplate.get("1", PoemDto.class);
//        assert poemDto != null;
//        System.out.println(poemDto);
//    }
//
//    /**
//     * 根据id删除
//     */
//    @Test
//    void deleteById() {
//        String delete = restTemplate.delete("1", PoemDto.class);
//        restTemplate.delete("2", PoemDto.class);
//        restTemplate.delete("3", PoemDto.class);
//        restTemplate.delete("4", PoemDto.class);
//        restTemplate.delete("5", PoemDto.class);
//        restTemplate.delete("6", PoemDto.class);
//        restTemplate.delete("7", PoemDto.class);
//        restTemplate.delete("8", PoemDto.class);
//        restTemplate.delete("9", PoemDto.class);
//        restTemplate.delete("10", PoemDto.class);
//        restTemplate.delete("11", PoemDto.class);
//        restTemplate.delete("12", PoemDto.class);
//        restTemplate.delete("13", PoemDto.class);
//        restTemplate.delete("14", PoemDto.class);
//        restTemplate.delete("15", PoemDto.class);
//        restTemplate.delete("16", PoemDto.class);
//        restTemplate.delete("17", PoemDto.class);
//        restTemplate.delete("18", PoemDto.class);
//        restTemplate.delete("19", PoemDto.class);
//        restTemplate.delete("20", PoemDto.class);
//        restTemplate.delete("21", PoemDto.class);
//        System.out.println(delete);
//    }
//
//    /**
//     * 查询所有
//     */
//    @Test
//    void searchAll() {
//
//        SearchHits<PoemDto> search = restTemplate.search(Query.findAll(), PoemDto.class);
//        List<SearchHit<PoemDto>> searchHits = search.getSearchHits();
//        // 获得searchHits,进行遍历得到content
//        List<PoemDto> poemDtos = new ArrayList<>();
//        searchHits.forEach(hit -> {
//            poemDtos.add(hit.getContent());
//        });
//        System.out.println(poemDtos);
//    }
//
//    /**
//     * match查询, 模糊查询
//     */
//    @Test
//    void matchQuery() {
//        // 创建查询
//        Query query = NativeQuery.builder().withQuery(q -> q
//                .multiMatch(m ->
//                        m.fields("author","title", "content").query("王维")
//                                 // 启用模糊匹配
//                                . fuzziness("AUTO"))
//
//        ).withPageable(Pageable.ofSize(10).withPage(0)).build();
//
//        SearchHits<PoemDto> searchHits = restTemplate.search(query, PoemDto.class);
//
//        // 获得searchHits,进行遍历得到content
//        List<PoemDto> poemDtos = new ArrayList<>();
//        searchHits.forEach(hit -> {
//            poemDtos.add(hit.getContent());
//        });
//        System.out.println(poemDtos);
//
//    }
//
//    /**
//     * 分页查询
//     */
//    @Test
//    void pageSearch() {
//        Query query = NativeQuery.builder().withQuery(Query.findAll())
//                .withPageable(Pageable.ofSize(3).withPage(0)).build();
//
//        SearchHits<PoemDto> searchHits = restTemplate.search(query, PoemDto.class);
//        // 获得searchHits,进行遍历得到content
//        List<PoemDto> poemDtos = new ArrayList<>();
//        searchHits.forEach(hit -> {
//            poemDtos.add(hit.getContent());
//        });
//        System.out.println(poemDtos);
//    }
//
//    /**
//     * 排序查询，根据id降序排列
//     */
//    @Test
//    void sortSearch() {
//        Query query = NativeQuery.builder().withQuery(Query.findAll())
//                .withPageable(Pageable.ofSize(10).withPage(0))
//                .withSort(Sort.by("id").descending()).build();
//
//        SearchHits<PoemDto> searchHits = restTemplate.search(query, PoemDto.class);
//        // 获得searchHits,进行遍历得到content
//        List<PoemDto> poemDtos = new ArrayList<>();
//        searchHits.forEach(hit -> {
//            poemDtos.add(hit.getContent());
//        });
//        System.out.println(poemDtos);
//    }
//}