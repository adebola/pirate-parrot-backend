package io.factorialsystems.msscpirateparrotproduct.controller;

import io.factorialsystems.msscpirateparrotproduct.dto.CategoryDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.MessageDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.service.CategoryService;
import io.factorialsystems.msscpirateparrotproduct.utils.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public PagedDTO<CategoryDTO> findAll(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return categoryService.findAll(pageNumber, pageSize);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findByName(@PathVariable("name") String name) {
        Optional<CategoryDTO> categoryDto = categoryService.findByName(name);

        if (categoryDto.isEmpty()) {
            final String errorMessage = String.format("Category with Name: %s Not Found", name);
            return ResponseEntity.badRequest().body(new MessageDTO(HttpStatus.BAD_REQUEST.value(), errorMessage));
        }

        return ResponseEntity.ok(categoryDto.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        Optional<CategoryDTO> categoryDto = categoryService.findById(id);

        if (categoryDto.isEmpty()) {
            final String errorMessage = String.format("Category with Id: %s Not Found", id);
            return ResponseEntity.badRequest().body(new MessageDTO(HttpStatus.BAD_REQUEST.value(), errorMessage));
        }

        return ResponseEntity.ok(categoryDto.get());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody CategoryDTO dto) {
        categoryService.clientSave(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(@PathVariable("id") String id, @Valid @RequestBody CategoryDTO dto) {
        categoryService.clientUpdate(id, dto);
    }

    @PutMapping("/suspend/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void suspend(@PathVariable("id") String id) {
        categoryService.clientSuspend(id);
    }

    @PutMapping("/unsuspend/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsuspend(@PathVariable("id") String id) {
        categoryService.clientUnsuspend(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        categoryService.clientDelete(id);
    }

    @GetMapping("/search/{search}")
    @ResponseStatus(HttpStatus.OK)
    public PagedDTO<CategoryDTO> search(@PathVariable("search") String search,
                                        @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return categoryService.search(pageNumber, pageSize, search);
    }
}
