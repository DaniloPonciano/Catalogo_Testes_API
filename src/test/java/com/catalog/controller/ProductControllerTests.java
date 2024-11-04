package com.catalog.controller;

import com.catalog.controllers.ProductController;
import com.catalog.dto.ProductDTO;
import com.catalog.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {
    //ok
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO(1L,"Test Product", "description", 9.99,"img/url", Instant.now());
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        List<ProductDTO> list = Arrays.asList(productDTO);
        Page<ProductDTO> page = new PageImpl<>(list);

        when(productService.findAllPaged(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(productDTO.getName()));
    }

    @Test
    void findByIdShouldReturnProduct() throws Exception {
        when(productService.findById(1L)).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(productDTO.getName()));
    }

    @Test
    void insertShouldReturnProductDTOCreated() throws Exception {
        when(productService.insert(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .content(objectMapper.writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(productDTO.getName()));
    }

    @Test
    void updateShouldReturnProductDTO() throws Exception {
        when(productService.update(any(Long.class), any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/products/1")
                        .content(objectMapper.writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(productDTO.getName()));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
