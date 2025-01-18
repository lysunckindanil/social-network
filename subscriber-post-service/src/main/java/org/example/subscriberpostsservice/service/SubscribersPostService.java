package org.example.subscriberpostsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.subscriberpostsservice.dto.PostDto;
import org.example.subscriberpostsservice.model.Post;
import org.example.subscriberpostsservice.model.Profile;
import org.example.subscriberpostsservice.repo.PostSubscriberRepository;
import org.example.subscriberpostsservice.repo.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscribersPostService {
    private final PostSubscriberRepository postSubscriberRepository;
    private final ProfileRepository profileRepository;

    public List<PostDto> getSubscribersPosts(String username) {
        Optional<Profile> profile_optional = profileRepository.findByUsername(username);
        if (profile_optional.isEmpty()) return new ArrayList<>();
        Profile profile = profile_optional.get();

        return postSubscriberRepository.findPostsBySubscriber(profile).stream().map(SubscribersPostService::wrapPost).toList();
    }

    private static PostDto wrapPost(Post post) {
        return PostDto.builder()
                .label(post.getLabel())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .author(post.getAuthor().getUsername())
                .build();
    }
}
