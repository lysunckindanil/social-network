package org.example.profileservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetProfilesPageableDto {
    @NotNull
    Integer page;
    @NotNull
    Integer size;
}
