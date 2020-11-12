package com.spring.boot.example.comment;

import br.com.fluentvalidator.AbstractValidator;
import com.spring.boot.example.comment.model.CommentParams;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

@Component
public class CommentDtoValidator extends AbstractValidator<CommentParams> {

    @Override
    public void rules() {
        ruleFor(CommentParams::getBody)
                .must(not(stringEmptyOrNull()))
                .withMessage("comment body must be not null or empty")
                .withFieldName("body");
    }

}
