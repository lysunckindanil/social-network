package org.example.webservice.dto.profiles;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetProfilesPageableDto {
    private final int page;
    private final int size;
}
