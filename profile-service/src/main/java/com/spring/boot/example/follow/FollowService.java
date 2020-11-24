package com.spring.boot.example.follow;

import com.spring.boot.example.follow.model.FollowRelation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public Mono<FollowRelation> saveRelation(FollowRelation followRelation) {
        return followRepository.findByUserIdAndFollowId(followRelation.getUserId(), followRelation.getFollowId())
                .switchIfEmpty(followRepository.save(followRelation));
    }

    public Mono<Void> removeRelation(FollowRelation followRelation) {
        return followRepository.delete(followRelation);
    }

}
