package org.example.subscriberpostsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.subscriberpostsservice.dto.GetPostsPageableDto;
import org.example.subscriberpostsservice.dto.PostDto;
import org.example.subscriberpostsservice.model.Post;
import org.example.subscriberpostsservice.model.Profile;
import org.example.subscriberpostsservice.repo.PostSubscriberRepository;
import org.example.subscriberpostsservice.repo.ProfileRepository;
import org.springframework.data.domain.PageRequest;
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

    public List<PostDto> getSubscribersPostsPageable(GetPostsPageableDto dto) {
        String username = dto.getProfile_username();
        int page = dto.getPage();
        int size = dto.getSize();
        Optional<Profile> author = profileRepository.findByUsername(username);
        if (author.isEmpty())
            return new ArrayList<>();

        PageRequest pageRequest = PageRequest.of(page, size);
        return postSubscriberRepository.findPostsBySubscriberPageableOrderByCreatedAtDesc(author.get(), pageRequest)
                .stream()
                .map(SubscribersPostService::wrapPost)
                .toList();
    }

    private static PostDto wrapPost(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .label(post.getLabel())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .author(post.getAuthor().getUsername())
                .build();
    }
}
