package com.example.bookstore.mainService.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Node
public class BookTag {
    @Id
    @GeneratedValue
    private Long id;

    private String tagName;

    private List<Long> bookIds;

    public BookTag(String name){
        this.tagName = name;
    }

    @Relationship(type = "child")
    public Set<BookTag> tagChildren;

    public void addChildTag(BookTag bookTag){
        if(this.tagChildren == null){
            this.tagChildren = new HashSet<>();
        }
        tagChildren.add(bookTag);
    }

    public void addBookID(Long id){
        if(bookIds == null)
            bookIds = new ArrayList<>();
        for(Long bid : bookIds){
            if(bid == id)
                return;
        }
        bookIds.add(id);
    }

    @JsonBackReference
    public Set<BookTag> getTagChildren(){
        return this.tagChildren;
    }

    @JsonBackReference
    public void setTagChildren(Set<BookTag> bookTags){
        this.tagChildren = bookTags;
    }
}
