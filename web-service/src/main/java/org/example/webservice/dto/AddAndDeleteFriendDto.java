package org.example.webservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddAndDeleteFriendDto {
    private String profile_username;
    private String friend_username;
}
