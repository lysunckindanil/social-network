package org.example.postsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.postsservice.dto.DeleteFromSubscribersDto;
import org.example.postsservice.dto.ShareFriendsDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShareSubscribersClient {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void shareSubscribers(Long profile_id, Long post_id) {
        ShareFriendsDto dto = ShareFriendsDto.builder().profile_id(profile_id).post_id(post_id).build();
        kafkaTemplate.send("share-subscribers", dto);
    }

    public void deleteFromSubscribers(Long profile_id, Long post_id) {
        DeleteFromSubscribersDto dto = DeleteFromSubscribersDto.builder().profile_id(profile_id).post_id(post_id).build();
        kafkaTemplate.send("delete-from-subscribers", dto);
    }
}
