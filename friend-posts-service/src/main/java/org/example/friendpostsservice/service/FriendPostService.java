package org.example.friendpostsservice.service;

import lombok.RequiredArgsConstructor;
import org.example.friendpostsservice.dto.PostDto;
import org.example.friendpostsservice.dto.PostsDto;
import org.example.friendpostsservice.model.FriendPost;
import org.example.friendpostsservice.model.Post;
import org.example.friendpostsservice.model.Profile;
import org.example.friendpostsservice.repo.FriendPostRepository;
import org.example.friendpostsservice.repo.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendPostService {
    private final FriendPostRepository friendPostRepository;
    private final ProfileRepository profileRepository;

    public PostsDto getFriendsPosts(String username) {
        Optional<Profile> profile_optional = profileRepository.findByUsername(username);
        if (profile_optional.isEmpty()) return PostsDto.builder().posts(new ArrayList<>()).build();

        Profile profile = profile_optional.get();
        Optional<FriendPost> friendPost_optional = friendPostRepository.findFriendPostByProfile(profile);
        if (friendPost_optional.isEmpty()) return PostsDto.builder().posts(new ArrayList<>()).build();

        FriendPost friendPost = friendPost_optional.get();
        return PostsDto.builder().posts(friendPost.getPosts().stream().map(FriendPostService::wrapPost).toList()).build();
    }

    private static PostDto wrapPost(Post post) {
        return PostDto.builder()
                .label(post.getLabel())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
