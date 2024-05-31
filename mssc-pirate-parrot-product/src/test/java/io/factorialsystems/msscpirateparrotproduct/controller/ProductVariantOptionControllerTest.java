package io.factorialsystems.msscpirateparrotproduct.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.ProductVariantOptionDTO;
import io.factorialsystems.msscpirateparrotproduct.service.ProductVariantOptionService;
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
@WebMvcTest(value = ProductVariantOptionController.class)
class ProductVariantOptionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductVariantOptionService productVariantOptionService;

    @Test
    void findAll() throws Exception {
        ProductVariantOptionDTO productVariantOptionDTO = new ProductVariantOptionDTO();
        productVariantOptionDTO.setId(UUID.randomUUID().toString());

        PagedDTO<ProductVariantOptionDTO> pagedDTO = new PagedDTO<>();
        pagedDTO.setList(List.of(productVariantOptionDTO));

        given(productVariantOptionService.findAll(anyInt(), anyInt())).willReturn(pagedDTO);

        final Integer pageSize = 20;
        final Integer pageNumber = 1;

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/pvo")
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

        verify(productVariantOptionService).findAll(pageNumberArgumentCaptor.capture(), pageSizeArgumentCaptor.capture());

        assertThat(pageNumberArgumentCaptor.getValue()).isEqualTo(pageNumber);
        assertThat(pageSizeArgumentCaptor.getValue()).isEqualTo(pageSize);
    }

    @Test
    void findByName() throws Exception {
        final String name = "name";
        final String id = UUID.randomUUID().toString();

        ProductVariantOptionDTO productVariantOptionDTO = new ProductVariantOptionDTO();
        productVariantOptionDTO.setId(id);
        productVariantOptionDTO.setName(name);

        given(productVariantOptionService.findByName(name)).willReturn(Optional.of(productVariantOptionDTO));

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/pvo/name/" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();

        log.info("Response {}", mvcResult.getResponse().getContentAsString());
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(productVariantOptionService).findByName(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(name);
    }

    @Test
    void findById() throws Exception {
        final String id = UUID.randomUUID().toString();
        ProductVariantOptionDTO productVariantOptionDTO = new ProductVariantOptionDTO();
        productVariantOptionDTO.setId(id);

        given(productVariantOptionService.findById(id)).willReturn(Optional.of(productVariantOptionDTO));

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/pvo/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();

        log.info("Response {}", mvcResult.getResponse().getContentAsString());
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(productVariantOptionService).findById(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(id);
    }

    @Test
    void create() throws Exception {
        ProductVariantOptionDTO c = new ProductVariantOptionDTO();
        c.setName("new-variant");

        mockMvc.perform(post("/api/v1/product/pvo")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isCreated())
                .andReturn();

        ArgumentCaptor<ProductVariantOptionDTO> categoryArgumentCaptor = ArgumentCaptor.forClass(ProductVariantOptionDTO.class);
        verify(productVariantOptionService).clientSave(categoryArgumentCaptor.capture());
        assertThat(c.getId()).isEqualTo(categoryArgumentCaptor.getValue().getId());
        assertThat(c.getName()).isEqualTo(categoryArgumentCaptor.getValue().getName());

        log.info("Argument Captor {}", categoryArgumentCaptor.getValue());
    }

    @Test
    void update() throws Exception {
        final ProductVariantOptionDTO c = new ProductVariantOptionDTO();
        c.setName("new-variant");

        final String id = UUID.randomUUID().toString();

        mockMvc.perform(put("/api/v1/product/pvo/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isAccepted())
                .andReturn();

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProductVariantOptionDTO> categoryArgumentCaptor = ArgumentCaptor.forClass(ProductVariantOptionDTO.class);

        verify(productVariantOptionService).clientUpdate(stringArgumentCaptor.capture(), categoryArgumentCaptor.capture());
        assertThat(c.getId()).isEqualTo(categoryArgumentCaptor.getValue().getId());
        assertThat(c.getName()).isEqualTo(categoryArgumentCaptor.getValue().getName());
        assertThat(id).isEqualTo(stringArgumentCaptor.getValue());
    }

    @Test
    void suspend() throws Exception {
        final String id = UUID.randomUUID().toString();

        mockMvc.perform(put("/api/v1/product/pvo/suspend/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void unsuspend() throws Exception {
        final String id = UUID.randomUUID().toString();

        mockMvc.perform(put("/api/v1/product/pvo/unsuspend/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void delete() {
    }

    @Test
    void search() throws Exception {
        ProductVariantOptionDTO productVariantOptionDTO = new ProductVariantOptionDTO();
        productVariantOptionDTO.setId(UUID.randomUUID().toString());

        PagedDTO<ProductVariantOptionDTO> pagedDTO = new PagedDTO<>();
        pagedDTO.setList(List.of(productVariantOptionDTO));

        given(productVariantOptionService.search(anyInt(), anyInt(), anyString())).willReturn(pagedDTO);

        final Integer pageSize = 20;
        final Integer pageNumber = 1;

        final String search = "do";

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/pvo/search/" + search)
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

        verify(productVariantOptionService).search(pageNumberArgumentCaptor.capture(), pageSizeArgumentCaptor.capture(), searchArgumentCaptor.capture());

        assertThat(pageNumberArgumentCaptor.getValue()).isEqualTo(pageNumber);
        assertThat(pageSizeArgumentCaptor.getValue()).isEqualTo(pageSize);
        assertThat(searchArgumentCaptor.getValue()).isEqualTo(search);
    }
}