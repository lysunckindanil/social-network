package org.example.webservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSubscribersPageableDto {
    String profileUsername;
    int page;
    int size;
}
