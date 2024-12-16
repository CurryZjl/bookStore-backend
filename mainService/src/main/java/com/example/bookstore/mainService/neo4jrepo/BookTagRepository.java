package com.example.bookstore.mainService.neo4jrepo;

import com.example.bookstore.mainService.entity.BookTag;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookTagRepository extends Neo4jRepository<BookTag, Long> {
    List<BookTag> findBookTagsByTagNameLike(String name);

    @Query(value = "MATCH (t:BookTag) - [:child]->(tc) WHERE t.tagName = $0 RETURN tc")
    List<BookTag> findNodesDistance1(String name);

    @Query(value = "MATCH (t:BookTag) - [:child]->(tc) - [:child]->(tcc) WHERE t.tagName=$0 RETURN tcc")
    List<BookTag> findNodesDistance2(String name);

}
