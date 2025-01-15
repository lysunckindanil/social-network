package org.example.postsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.postsservice.dto.DeleteFromFriendsDto;
import org.example.postsservice.dto.ShareFriendsDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShareFriendsClient {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void shareFriends(Long profile_id, Long post_id) {
        ShareFriendsDto dto = ShareFriendsDto.builder().profile_id(profile_id).post_id(post_id).build();
        kafkaTemplate.send("share-friends", dto);
    }

    public void deleteFromFriends(Long profile_id, Long post_id) {
        DeleteFromFriendsDto dto = DeleteFromFriendsDto.builder().profile_id(profile_id).post_id(post_id).build();
        kafkaTemplate.send("delete-from-friends", dto);
    }
}
