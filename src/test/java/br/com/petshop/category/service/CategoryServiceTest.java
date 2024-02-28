package br.com.petshop.category.service;

import br.com.petshop.category.model.dto.request.CategoryUpdateRequest;
import br.com.petshop.category.model.dto.request.CategoryUpdateRequestMock;
import br.com.petshop.category.model.dto.response.CategoryResponse;
import br.com.petshop.category.model.dto.response.CategoryResponseMock;
import br.com.petshop.category.model.entity.CategoryEntity;
import br.com.petshop.category.model.entity.CategoryEntityMock;
import br.com.petshop.category.model.enums.Category;
import br.com.petshop.category.repository.CategoryRepository;
import br.com.petshop.category.repository.CategorySpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

class CategoryServiceTest {
    @Mock CategoryRepository repository;
    @Mock ObjectMapper objectMapper;
    @Mock CategorySpecification specification;
    @InjectMocks CategoryService categoryService;
    @Mock Specification<CategoryEntity> filter;
    @Mock JsonPatch patch;
    CategoryEntity entity;
    CategoryUpdateRequest updateRequest;
    CategoryResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CategoryEntityMock.get();
        updateRequest = CategoryUpdateRequestMock.get();
        response = CategoryResponseMock.get();
    }

    @Test
    void testSave() {
        when(repository.save(entity)).thenReturn(entity);

        CategoryEntity result = categoryService.save(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateById() {
        when(repository.save(entity)).thenReturn(entity);

        CategoryEntity result = categoryService.updateById(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testActivate() throws JsonPatchException, JsonProcessingException {
        when(repository.save(entity)).thenReturn(entity);

        CategoryEntity result = categoryService.activate(CategoryEntityMock.get(), patch);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CategoryEntity result = categoryService.findById(id);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testFindByIdAndActiveIsTrue() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CategoryEntity result = categoryService.findByIdAndActiveIsTrue(id);
        Assertions.assertEquals(new CategoryEntity(null, Category.PETCARE, "description", Boolean.TRUE), result);
    }

    @Test
    void testFindAllByCompanyId() {
        when(repository.findAll(filter)).thenReturn(List.of(entity));

        List<CategoryEntity> result = categoryService.findAllByCompanyId(UUID.randomUUID(), Boolean.TRUE);
        Assertions.assertEquals(0, result.size() );
    }
}