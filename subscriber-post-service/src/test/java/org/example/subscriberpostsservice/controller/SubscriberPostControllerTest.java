package org.example.subscriberpostsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.subscriberpostsservice.dto.GetPostsPageableDto;
import org.example.subscriberpostsservice.service.SubscribersPostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = SubscriberPostController.class)
@ExtendWith(SpringExtension.class)
class SubscriberPostControllerTest {
    @MockitoBean
    SubscribersPostService subscribersPostService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getByUsernamePageable_InvalidData_ThrowsException() throws Exception {
        mockMvc.perform(
                post("/getByUsernamePageable")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(subscribersPostService, Mockito.never()).getSubscribersPostsPageable(Mockito.any());

        GetPostsPageableDto dto = GetPostsPageableDto.builder()
                .profileUsername("username")
                .page(1)
                .build();
        mockMvc.perform(
                post("/getByUsernamePageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(subscribersPostService, Mockito.never()).getSubscribersPostsPageable(Mockito.any());
    }

    @Test
    void getByUsernamePageable_ValidData_ServiceCalled() throws Exception {
        GetPostsPageableDto dto = GetPostsPageableDto.builder()
                .profileUsername("username")
                .page(1)
                .size(1)
                .build();

        mockMvc.perform(
                post("/getByUsernamePageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(subscribersPostService, Mockito.times(1))
                .getSubscribersPostsPageable(Mockito.any());
    }

}