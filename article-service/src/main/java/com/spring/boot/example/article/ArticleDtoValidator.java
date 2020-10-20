package com.spring.boot.example.article;

import br.com.fluentvalidator.AbstractValidator;
import com.spring.boot.example.article.model.ArticleDto;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

@Component
public class ArticleDtoValidator extends AbstractValidator<ArticleDto> {

    @Override
    public void rules() {
        ruleFor(ArticleDto::getTitle)
                .must(not(stringEmptyOrNull()))
                .withMessage("article title must be not null or empty")
                .withFieldName("title");

        ruleFor(ArticleDto::getDescription)
                .must(not(nullValue()))
                .withMessage("article description must be not null")
                .withFieldName("description");

        ruleFor(ArticleDto::getBody)
                .must(not(nullValue()))
                .withMessage("article body must be not null")
                .withFieldName("body");
    }

}
