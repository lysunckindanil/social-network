package org.example.postsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.postsservice.dto.DeletePostDto;
import org.example.postsservice.dto.ShareFriendsDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShareSubscribersClient {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void shareSubscribers(Long profileId, Long postId) {
        ShareFriendsDto dto = ShareFriendsDto.builder().profileId(profileId).postId(postId).build();
        kafkaTemplate.send("share-subscribers", dto);
    }

    public void deleteFromSubscribers(Long postId) {
        DeletePostDto dto = DeletePostDto.builder().postId(postId).build();
        kafkaTemplate.send("delete-from-subscribers", dto);
    }
}
