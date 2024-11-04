package com.catalog.repository;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.catalog.entities.Product;
import com.catalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        //Preparação dos dados
        long existingId = 1L;

        //Executando a ação
        repository.deleteById(existingId);

        //Verificando se a ação executou corretamente
        Optional<Product> result = repository.findById(existingId);
        
        //Verifica se o objeto ainda existe
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("")
    public void updateShouldChangeAndPersistDataWhenIdExists(){
        //Preparação dos dados
        Product product = new Product(1L, "Phone", "Smartphone", 1850.00, 
        "imgProduto", Instant.now());

        //Executando a ação
        product.setName("Update Phone");
        product.setPrice(5000.00);
        product = repository.save(product);

        //Verificando se a ação executou como deveria
        Assertions.assertEquals("Update Phone", product.getName());
    }    

    @Test
    public void findByIdShoulReturnNonEmptyOptionalWhenExists(){
        //Preparação dos dados
        long existingId = 1L;

        //Executando a ação
        Optional<Product> result = repository.findById(existingId);

        //Verificando se a ação executou corretamente
        Assertions.assertTrue(result.isPresent());
    }
}
