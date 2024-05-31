package io.factorialsystems.msscpirateparrotproduct.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.UomDTO;
import io.factorialsystems.msscpirateparrotproduct.service.UomService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = UomController.class)
class UomControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UomService uomService;

    @Test
    void findAll() throws Exception {
        UomDTO uomDTO = new UomDTO();
        uomDTO.setId(UUID.randomUUID().toString());

        PagedDTO<UomDTO> pagedDTO = new PagedDTO<>();
        pagedDTO.setList(List.of(uomDTO));

        given(uomService.findAll(anyInt(), anyInt())).willReturn(pagedDTO);

        final Integer pageSize = 20;
        final Integer pageNumber = 1;

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/uom")
                        .queryParam("pageNumber", String.valueOf(pageNumber))
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(5)))
                .andReturn();

        log.info("Response {}", mvcResult.getResponse().getContentAsString());
        ArgumentCaptor<Integer> pageNumberArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> pageSizeArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(uomService).findAll(pageNumberArgumentCaptor.capture(), pageSizeArgumentCaptor.capture());

        assertThat(pageNumberArgumentCaptor.getValue()).isEqualTo(pageNumber);
        assertThat(pageSizeArgumentCaptor.getValue()).isEqualTo(pageSize);
    }

    @Test
    void findById() throws Exception {
        final String id = UUID.randomUUID().toString();
        UomDTO uomDTO = new UomDTO();
        uomDTO.setId(id);

        given(uomService.findById(id)).willReturn(Optional.of(uomDTO));

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/uom/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();

        log.info("Response {}", mvcResult.getResponse().getContentAsString());
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(uomService).findById(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(id);
    }

    @Test
    void findByName() throws Exception {
        final String name = "name";
        final String id = UUID.randomUUID().toString();

        UomDTO uomDTO = new UomDTO();
        uomDTO.setId(id);
        uomDTO.setName(name);

        given(uomService.findByName(name)).willReturn(Optional.of(uomDTO));

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/uom/name/" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();

        log.info("Response {}", mvcResult.getResponse().getContentAsString());
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(uomService).findByName(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(name);
    }


    @Test
    void create() throws Exception {
        UomDTO c = new UomDTO();
        c.setName("new-uom");

        mockMvc.perform(post("/api/v1/product/uom")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isCreated())
                .andReturn();

        ArgumentCaptor<UomDTO> categoryArgumentCaptor = ArgumentCaptor.forClass(UomDTO.class);
        verify(uomService).clientSave(categoryArgumentCaptor.capture());
        assertThat(c.getId()).isEqualTo(categoryArgumentCaptor.getValue().getId());
        assertThat(c.getName()).isEqualTo(categoryArgumentCaptor.getValue().getName());

        log.info("Argument Captor {}", categoryArgumentCaptor.getValue());
    }

    @Test
    void update() throws Exception {
        final UomDTO c = new UomDTO();
        c.setName("new-uom");

        final String id = UUID.randomUUID().toString();

        mockMvc.perform(put("/api/v1/product/uom/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isAccepted())
                .andReturn();

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UomDTO> categoryArgumentCaptor = ArgumentCaptor.forClass(UomDTO.class);

        verify(uomService).clientUpdate(stringArgumentCaptor.capture(), categoryArgumentCaptor.capture());
        assertThat(c.getId()).isEqualTo(categoryArgumentCaptor.getValue().getId());
        assertThat(c.getName()).isEqualTo(categoryArgumentCaptor.getValue().getName());
        assertThat(id).isEqualTo(stringArgumentCaptor.getValue());
    }

    @Test
    void suspend() throws Exception {
        final String id = UUID.randomUUID().toString();

        mockMvc.perform(put("/api/v1/product/uom/suspend/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void unsuspend() throws Exception {
        final String id = UUID.randomUUID().toString();

        mockMvc.perform(put("/api/v1/product/uom/unsuspend/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void search() throws Exception {
        UomDTO uomDTO = new UomDTO();
        uomDTO.setId(UUID.randomUUID().toString());

        PagedDTO<UomDTO> pagedDTO = new PagedDTO<>();
        pagedDTO.setList(List.of(uomDTO));

        given(uomService.search(anyInt(), anyInt(), anyString())).willReturn(pagedDTO);

        final Integer pageSize = 20;
        final Integer pageNumber = 1;

        final String search = "An";

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/uom/search/" + search)
                        .queryParam("pageNumber", String.valueOf(pageNumber))
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(5)))
                .andReturn();

        log.info("Response {}", mvcResult.getResponse().getContentAsString());
        ArgumentCaptor<Integer> pageNumberArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> pageSizeArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> searchArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(uomService).search(pageNumberArgumentCaptor.capture(), pageSizeArgumentCaptor.capture(), searchArgumentCaptor.capture());

        assertThat(pageNumberArgumentCaptor.getValue()).isEqualTo(pageNumber);
        assertThat(pageSizeArgumentCaptor.getValue()).isEqualTo(pageSize);
        assertThat(searchArgumentCaptor.getValue()).isEqualTo(search);
    }
}