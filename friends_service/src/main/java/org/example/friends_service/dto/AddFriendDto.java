package org.example.friends_service.dto;

import lombok.Data;

@Data
public class AddFriendDto {
    private String profile_username;
    private String friend_username;
}
