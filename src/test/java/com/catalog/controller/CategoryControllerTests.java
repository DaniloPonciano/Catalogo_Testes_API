package com.catalog.controller;

import com.catalog.controllers.CategoryController;
import com.catalog.dto.CategoryDTO;
import com.catalog.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        categoryDTO = new CategoryDTO(1L, "Test Category");
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        List<CategoryDTO> list = Arrays.asList(categoryDTO);
        Page<CategoryDTO> page = new PageImpl<>(list);

        when(categoryService.findAllPaged(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(categoryDTO.getName()));
    }

    @Test
    void findByIdShouldReturnCategory() throws Exception {
        when(categoryService.findById(1L)).thenReturn(categoryDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(categoryDTO.getName()));
    }

    @Test
    void insertShouldReturnCategoryDTOCreated() throws Exception {
        when(categoryService.insert(any(CategoryDTO.class))).thenReturn(categoryDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .content(objectMapper.writeValueAsString(categoryDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(categoryDTO.getName()));
    }

    @Test
    void updateShouldReturnCategoryDTO() throws Exception {
        when(categoryService.update(any(Long.class), any(CategoryDTO.class))).thenReturn(categoryDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/categories/1")
                        .content(objectMapper.writeValueAsString(categoryDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(categoryDTO.getName()));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}