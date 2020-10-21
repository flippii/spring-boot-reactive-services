package com.spring.boot.example.comment.model;

import com.spring.boot.example.core.domain.AbstractAuditingDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "comments")
public class Comment extends AbstractAuditingDocument<String> {

    @Id
    private String id;
    private String body;
    private String articleId;

    public Comment(String body, String articleId) {
        this.body = body;
        this.articleId = articleId;
    }

}


