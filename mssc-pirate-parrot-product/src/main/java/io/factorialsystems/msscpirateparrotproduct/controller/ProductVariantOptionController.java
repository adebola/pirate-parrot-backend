package io.factorialsystems.msscpirateparrotproduct.controller;

import io.factorialsystems.msscpirateparrotproduct.dto.MessageDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.ProductVariantOptionDTO;
import io.factorialsystems.msscpirateparrotproduct.service.ProductVariantOptionService;
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
@RequestMapping("/api/v1/product/pvo")
public class ProductVariantOptionController {
    private final ProductVariantOptionService productVariantOptionService;

    @GetMapping
    public PagedDTO<ProductVariantOptionDTO> findAll(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return productVariantOptionService.findAll(pageNumber, pageSize);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findByName(@PathVariable("name") String name) {
        Optional<ProductVariantOptionDTO> productVariantDTO = productVariantOptionService.findByName(name);

        if (productVariantDTO.isEmpty()) {
            final String errorMessage = String.format("Product Variant Option with Name: %s Not Found", name);
            return ResponseEntity.badRequest().body(new MessageDTO(HttpStatus.BAD_REQUEST.value(), errorMessage));
        }

        return ResponseEntity.ok(productVariantDTO.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        Optional<ProductVariantOptionDTO> dto = productVariantOptionService.findById(id);

        if (dto.isEmpty()) {
            final String errorMessage = String.format("Product Variant Option with Id: %s Not Found", id);
            return ResponseEntity.badRequest().body(new MessageDTO(HttpStatus.BAD_REQUEST.value(), errorMessage));
        }

        return ResponseEntity.ok(dto.get());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody ProductVariantOptionDTO dto) {
        productVariantOptionService.clientSave(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(@PathVariable("id") String id, @Valid @RequestBody ProductVariantOptionDTO dto) {
        productVariantOptionService.clientUpdate(id, dto);
    }

    @PutMapping("/suspend/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void suspend(@PathVariable("id") String id) {
        productVariantOptionService.clientSuspend(id);
    }

    @PutMapping("/unsuspend/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsuspend(@PathVariable("id") String id) {
        productVariantOptionService.clientUnsuspend(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        productVariantOptionService.clientDelete(id);
    }

    @GetMapping("/search/{search}")
    @ResponseStatus(HttpStatus.OK)
    public PagedDTO<ProductVariantOptionDTO> search(@PathVariable("search") String search,
                                              @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return productVariantOptionService.search(pageNumber, pageSize, search);
    }
}
