package io.factorialsystems.msscpirateparrotproduct.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.factorialsystems.msscpirateparrotproduct.dto.CategoryDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.model.Category;
import io.factorialsystems.msscpirateparrotproduct.repository.CategoryRepository;
import io.factorialsystems.msscpirateparrotproduct.security.JwtTokenWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void clientSave(CategoryDTO categoryDto) {
        Category c = Category.createCategory(categoryDto.getName(), categoryDto.getImageUrl());
        log.info("Saving category {}", c);
        categoryRepository.save(c);
    }

    public void clientUpdate (String id, CategoryDTO categoryDto) {
        categoryDto.setId(id);
        Category c = Category.createCategoryFromDto(categoryDto);
        log.info("updating Category {}", c);
        categoryRepository.update(c);
    }

    public PagedDTO<CategoryDTO> findAll(Integer pageNumber, Integer pageSize) {
        log.info("Find All Categories PageNumber : {}, PageSize {}", pageNumber, pageSize);

        try (var ignored = PageHelper.startPage(pageNumber, pageSize)) {
            return createClientDto(categoryRepository.findAll());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Optional<CategoryDTO> findById(String id) {
        log.info("Find Category by Id {}", id);
        Category c = categoryRepository.findById(id);
        return (c == null) ? Optional.empty() : Optional.of(c.createDto());
    }

    public Optional<CategoryDTO> findByName(String name) {
        log.info("Find Category by Name {}", name);
        Category c = categoryRepository.findByName(name);
        return (c == null) ? Optional.empty() : Optional.of(c.createDto());
    }

    public PagedDTO<CategoryDTO> search(Integer pageNumber, Integer pageSize, String searchString) {
        log.info("Search Category PageNumber {}, PageSize {}, search String {}", pageNumber, pageSize, searchString);

        try (var ignored = PageHelper.startPage(pageNumber, pageSize)) {
            return createClientDto(categoryRepository.search(searchString));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void clientDelete(String id) {
        log.info("Delete Category {}, deleted By {}", id, JwtTokenWrapper.getUserName());
    }

    public void clientSuspend(String id) {
        log.info("Suspend Category {}", id);
        categoryRepository.suspend(id);
    }

    public void clientUnsuspend(String id) {
        log.info("Unsuspend Category {}", id);
        categoryRepository.unsuspend(id);
    }

    private PagedDTO<CategoryDTO> createClientDto(Page<Category> categories) {
        List<Category> result = categories.getResult();

        PagedDTO<CategoryDTO> pagedDto = new PagedDTO<>();
        pagedDto.setTotalSize((int) categories.getTotal());
        pagedDto.setPageNumber(categories.getPageNum());
        pagedDto.setPageSize(categories.getPageSize());
        pagedDto.setPages(categories.getPages());
        pagedDto.setList(result.stream().map(Category::createDto).toList());

        return pagedDto;
    }
}
