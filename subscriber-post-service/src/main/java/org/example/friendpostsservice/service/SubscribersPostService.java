package org.example.friendpostsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.friendpostsservice.dto.PostDto;
import org.example.friendpostsservice.model.Post;
import org.example.friendpostsservice.model.Profile;
import org.example.friendpostsservice.model.ProfilePostSubscriber;
import org.example.friendpostsservice.repo.ProfilePostSubscribedRepository;
import org.example.friendpostsservice.repo.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscribersPostService {
    private final ProfilePostSubscribedRepository profilePostSubscribedRepository;
    private final ProfileRepository profileRepository;

    public List<PostDto> getFriendsPosts(String username) {
        Optional<Profile> profile_optional = profileRepository.findByUsername(username);
        if (profile_optional.isEmpty()) return new ArrayList<>();
        Profile profile = profile_optional.get();

        Optional<ProfilePostSubscriber> friendPost_optional = profilePostSubscribedRepository.findProfilePostSubscribedByProfile(profile);
        if (friendPost_optional.isEmpty()) return new ArrayList<>();

        ProfilePostSubscriber profilePostSubscriber = friendPost_optional.get();
        return profilePostSubscriber.getPosts().stream().map(SubscribersPostService::wrapPost).toList();
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
