package org.example.friends_service.dto;

import lombok.Data;

@Data
public class AddAndDeleteFriendDto {
    private String profile_username;
    private String friend_username;
}
