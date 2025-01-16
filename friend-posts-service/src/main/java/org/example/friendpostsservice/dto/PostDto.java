package org.example.friendpostsservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class PostDto implements Comparable<PostDto> {
    private String label;
    private String content;
    private Date createdAt;

    @Override
    public int compareTo(PostDto o) {
        return -1 * this.createdAt.compareTo(o.createdAt);
    }
}
