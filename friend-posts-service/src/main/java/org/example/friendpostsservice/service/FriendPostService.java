package org.example.friendpostsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.friendpostsservice.dto.PostDto;
import org.example.friendpostsservice.model.ProfileSubscribedByPost;
import org.example.friendpostsservice.model.Post;
import org.example.friendpostsservice.model.Profile;
import org.example.friendpostsservice.repo.ProfileSubscribedByPostRepository;
import org.example.friendpostsservice.repo.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendPostService {
    private final ProfileSubscribedByPostRepository profileSubscribedByPostRepository;
    private final ProfileRepository profileRepository;

    public List<PostDto> getFriendsPosts(String username) {
        Optional<Profile> profile_optional = profileRepository.findByUsername(username);
        if (profile_optional.isEmpty()) return new ArrayList<>();

        Profile profile = profile_optional.get();
        Optional<ProfileSubscribedByPost> friendPost_optional = profileSubscribedByPostRepository.findFriendPostByProfile(profile);
        if (friendPost_optional.isEmpty()) return new ArrayList<>();

        ProfileSubscribedByPost profileSubscribedByPost = friendPost_optional.get();
        return profileSubscribedByPost.getPosts().stream().map(FriendPostService::wrapPost).sorted().toList();
    }

    private static PostDto wrapPost(Post post) {
        return PostDto.builder()
                .label(post.getLabel())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
