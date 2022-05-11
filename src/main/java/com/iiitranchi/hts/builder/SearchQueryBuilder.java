package com.iiitranchi.hts.builder;

import com.iiitranchi.hts.model.Book;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class SearchQueryBuilder {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public SearchHits<Book> getRelatedBooks(String text) {

        /*
         * here,Fuzziness specifies maximum edit distance for matching.
         * Edit distance example :
         * Changing a character (box → fox)
         * Removing a character (black → lack)
         * Inserting a character (sic → sick)
         * Transposing two adjacent characters (act → cat)
         * By default 80% humans have a fuzziness of ONE i.e one spelling mistake.Auto
         * sets it to two.
         */

        MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery(text, "content", "name")
                .fuzziness(Fuzziness.AUTO);

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(matchQueryBuilder).withMaxResults(3)
                .build();
        return elasticsearchRestTemplate.search(build, Book.class);
    }

    public SearchHits<Book> getBookByName(String text) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(text, "name");

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(matchQueryBuilder).withMaxResults(1)
                .build();
        return elasticsearchRestTemplate.search(build, Book.class);
    }
}
