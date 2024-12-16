package com.example.bookstore.mainService.neo4jrepository;

import com.example.bookstore.mainService.entity.BookTag;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;

public interface BookTagRepository extends Neo4jRepository<BookTag, Long> {
    List<BookTag> findBookTagsByTagNameLike(String name);

    @Query(value = "MATCH (t:BookTag) - [:tagChildren]->(tc) WHERE t.tagName = $0 RETURN tc")
    List<BookTag> findNodesDistance1(String name);

    @Query(value = "MATCH (t:BookTag) - [:tagChildren]->(tc) - [:tagChildren]->(tcc) WHERE t.tagName=$0 RETURN tcc")
    List<BookTag> findNodesDistance2(String name);

    @Override
    void deleteAll();
}