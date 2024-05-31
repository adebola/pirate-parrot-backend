package io.factorialsystems.msscpirateparrotproduct.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.factorialsystems.msscpirateparrotproduct.dto.CategoryDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.service.CategoryService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = CategoryController.class)
class CategoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CategoryService categoryService;

    @Test
//    @WithMockUser(username="spring")
    void findAll() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(UUID.randomUUID().toString());

        PagedDTO<CategoryDTO> pagedDTO = new PagedDTO<>();
        pagedDTO.setList(List.of(categoryDTO));

        given(categoryService.findAll(anyInt(), anyInt())).willReturn(pagedDTO);

        final Integer pageSize = 20;
        final Integer pageNumber = 1;

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/category")
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

        verify(categoryService).findAll(pageNumberArgumentCaptor.capture(), pageSizeArgumentCaptor.capture());

        assertThat(pageNumberArgumentCaptor.getValue()).isEqualTo(pageNumber);
        assertThat(pageSizeArgumentCaptor.getValue()).isEqualTo(pageSize);
    }

    @Test
    void findById() throws Exception {
        final String id = UUID.randomUUID().toString();
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(id);

        given(categoryService.findById(id)).willReturn(Optional.of(categoryDTO));

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/category/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andReturn();

        log.info("Response {}", mvcResult.getResponse().getContentAsString());
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(categoryService).findById(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(id);
    }

    @Test
    void findByName() throws Exception {
        final String name = "name";
        final String id = UUID.randomUUID().toString();

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(id);
        categoryDTO.setName(name);

        given(categoryService.findByName(name)).willReturn(Optional.of(categoryDTO));

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/category/name/" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andReturn();

        log.info("Response {}", mvcResult.getResponse().getContentAsString());
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(categoryService).findByName(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(name);
    }


    @Test
    void create() throws Exception {
        CategoryDTO c = new CategoryDTO();
        c.setName("new-category");
        c.setImageUrl("image-url");

        mockMvc.perform(post("/api/v1/product/category")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isCreated())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ArgumentCaptor<CategoryDTO> categoryArgumentCaptor = ArgumentCaptor.forClass(CategoryDTO.class);
        verify(categoryService).clientSave(categoryArgumentCaptor.capture());
        assertThat(c.getId()).isEqualTo(categoryArgumentCaptor.getValue().getId());
        assertThat(c.getName()).isEqualTo(categoryArgumentCaptor.getValue().getName());

        log.info("Argument Captor {}", categoryArgumentCaptor.getValue());
    }

    @Test
    void update() throws Exception {
        final CategoryDTO c = new CategoryDTO();
        c.setName("new-category");
        c.setImageUrl("image-url");

        final String id = UUID.randomUUID().toString();

        mockMvc.perform(put("/api/v1/product/category/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isAccepted())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<CategoryDTO> categoryArgumentCaptor = ArgumentCaptor.forClass(CategoryDTO.class);

        verify(categoryService).clientUpdate(stringArgumentCaptor.capture(), categoryArgumentCaptor.capture());
        assertThat(c.getId()).isEqualTo(categoryArgumentCaptor.getValue().getId());
        assertThat(c.getName()).isEqualTo(categoryArgumentCaptor.getValue().getName());
        assertThat(id).isEqualTo(stringArgumentCaptor.getValue());
    }

    @Test
    void suspend() throws Exception {
        final String id = UUID.randomUUID().toString();

        mockMvc.perform(put("/api/v1/product/category/suspend/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void unsuspend() throws Exception {
        final String id = UUID.randomUUID().toString();

        mockMvc.perform(put("/api/v1/product/category/unsuspend/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void search() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(UUID.randomUUID().toString());

        PagedDTO<CategoryDTO> pagedDTO = new PagedDTO<>();
        pagedDTO.setList(List.of(categoryDTO));

        given(categoryService.search(anyInt(), anyInt(), anyString())).willReturn(pagedDTO);

        final Integer pageSize = 20;
        final Integer pageNumber = 1;

        final String search = "An";

        final MvcResult mvcResult = mockMvc.perform(get("/api/v1/product/category/search/" + search)
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

        verify(categoryService).search(pageNumberArgumentCaptor.capture(), pageSizeArgumentCaptor.capture(), searchArgumentCaptor.capture());

        assertThat(pageNumberArgumentCaptor.getValue()).isEqualTo(pageNumber);
        assertThat(pageSizeArgumentCaptor.getValue()).isEqualTo(pageSize);
        assertThat(searchArgumentCaptor.getValue()).isEqualTo(search);
    }
}