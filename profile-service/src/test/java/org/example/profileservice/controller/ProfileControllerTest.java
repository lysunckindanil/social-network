package org.example.profileservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.profileservice.dto.GetProfilesPageableDto;
import org.example.profileservice.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(ProfileController.class)
@ExtendWith(SpringExtension.class)
class ProfileControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private ProfileService profileService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void getByUsername_Empty_Throws() throws Exception {
        mvc.perform(
                post("/getByUsername")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getByUsername_Full_Ok() throws Exception {
        mvc.perform(
                post("/getByUsername")
                        .content("username")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllPageable_EmptyAnyField_Throws() throws Exception {
        GetProfilesPageableDto dto = GetProfilesPageableDto.builder().page(1).build();
        mvc.perform(
                post("/getAllPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
        dto.setSize(1);
        dto.setPage(null);
        mvc.perform(
                post("/getAllPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getAllPageable_Full_Ok() throws Exception {
        GetProfilesPageableDto dto = GetProfilesPageableDto.builder().page(1).size(1).build();
        mvc.perform(
                post("/getAllPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}