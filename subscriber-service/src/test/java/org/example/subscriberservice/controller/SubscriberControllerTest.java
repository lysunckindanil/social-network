package org.example.subscriberservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.subscriberservice.dto.AddAndDeleteSubscriberDto;
import org.example.subscriberservice.dto.GetSubscribersPageableDto;
import org.example.subscriberservice.dto.IsSubscriberDto;
import org.example.subscriberservice.service.SubscriberService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(SubscriberController.class)
@ExtendWith(SpringExtension.class)
class SubscriberControllerTest {

    @MockitoBean
    SubscriberService subscriberService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;


    @Test
    void findProfileSubscribedByPageable_EmptyDto_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/findProfileSubscribedByPageable"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void isSubscribedOn_EmptyAnyField_ThrowsException() throws Exception {
        IsSubscriberDto dto = IsSubscriberDto.builder()
                .profileUsername("username")
                .subscriberUsername("username")
                .build();
        Mockito.when(subscriberService.isProfileSubscribedOn(dto)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/isSubscribedOn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void isSubscribedOn_CorrectDto_Ok() throws Exception {
        IsSubscriberDto dto = IsSubscriberDto.builder()
                .profileUsername("username").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/isSubscribedOn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        dto.setSubscriberUsername("username");
        dto.setProfileUsername(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/isSubscribedOn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void findProfileSubscribedByPageable_EmptyAnyField_ThrowsException() throws Exception {
        GetSubscribersPageableDto dto = GetSubscribersPageableDto.builder().build();
        dto.setProfileUsername("username");

        mockMvc.perform(MockMvcRequestBuilders.post("/findProfileSubscribedByPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        dto.setPage(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/findProfileSubscribedByPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void findProfileSubscribedByPageable_CorrectDto_Ok() throws Exception {
        GetSubscribersPageableDto dto = GetSubscribersPageableDto.builder()
                .profileUsername("username")
                .page(1)
                .size(1)
                .build();
        Mockito.when(subscriberService.findProfileSubscribedByPageable(dto)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/findProfileSubscribedByPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findProfileSubscribedOnPageable_EmptyDto_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/findProfileSubscribedOnPageable"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void findProfileSubscribedOnPageable_EmptyAnyField_ThrowsException() throws Exception {
        GetSubscribersPageableDto dto = GetSubscribersPageableDto.builder().build();
        dto.setProfileUsername("username");

        mockMvc.perform(MockMvcRequestBuilders.post("/findProfileSubscribedOnPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        dto.setPage(1);
        mockMvc.perform(MockMvcRequestBuilders.post("/findProfileSubscribedOnPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void findProfileSubscribedOnPageable_CorrectDto_Ok() throws Exception {
        GetSubscribersPageableDto dto = GetSubscribersPageableDto.builder()
                .profileUsername("username")
                .page(1)
                .size(1)
                .build();
        Mockito.when(subscriberService.findProfileSubscribedOnPageable(dto)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/findProfileSubscribedOnPageable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void addSubscriber_EmptyDto_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/addSubscriber"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void addSubscriber_EmptyAnyField_ThrowsException() throws Exception {
        AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder()
                .profileUsername("username")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/addSubscriber").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        dto.setSubscriberUsername("username");
        dto.setProfileUsername(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/addSubscriber").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void addSubscriber_CorrectDto_NoContent() throws Exception {
        AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder()
                .profileUsername("username")
                .subscriberUsername("username")
                .build();

        Mockito.doNothing().when(subscriberService).addSubscriber(dto);
        mockMvc.perform(MockMvcRequestBuilders.post("/addSubscriber").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }


    @Test
    void deleteSubscriber_EmptyDto_ThrowsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/deleteSubscriber"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteSubscriber_EmptyAnyField_ThrowsException() throws Exception {
        AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder()
                .profileUsername("username")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/deleteSubscriber").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        dto.setSubscriberUsername("username");
        dto.setProfileUsername(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/deleteSubscriber").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteSubscriber_CorrectDto_NoContent() throws Exception {
        AddAndDeleteSubscriberDto dto = AddAndDeleteSubscriberDto.builder()
                .profileUsername("username")
                .subscriberUsername("username")
                .build();

        Mockito.doNothing().when(subscriberService).deleteSubscriber(dto);
        mockMvc.perform(MockMvcRequestBuilders.post("/deleteSubscriber").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}