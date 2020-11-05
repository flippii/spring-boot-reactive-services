package com.spring.boot.example.profile;

import br.com.fluentvalidator.AbstractValidator;
import com.spring.boot.example.profile.model.ProfileParams;
import org.springframework.stereotype.Component;

import static br.com.fluentvalidator.predicate.LogicalPredicate.not;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

@Component
public class ProfileParamsValidator extends AbstractValidator<ProfileParams> {

    @Override
    public void rules() {
        ruleFor(ProfileParams::getFirstName)
                .must(not(stringEmptyOrNull()))
                .withMessage("profile firstName must be not null or empty")
                .withFieldName("firstName");

        ruleFor(ProfileParams::getLastName)
                .must(not(stringEmptyOrNull()))
                .withMessage("profile lastName must be not null or empty")
                .withFieldName("lastName");

        ruleFor(ProfileParams::getBio)
                .must(not(nullValue()))
                .withMessage("profile bio must be not null")
                .withFieldName("bio");

        ruleFor(ProfileParams::getImageUrl)
                .must(not(nullValue()))
                .withMessage("profile imageUrl must be not null")
                .withFieldName("imageUrl");
    }

}
