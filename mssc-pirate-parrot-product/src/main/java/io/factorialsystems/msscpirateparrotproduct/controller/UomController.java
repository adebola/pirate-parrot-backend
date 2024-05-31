package io.factorialsystems.msscpirateparrotproduct.controller;

import io.factorialsystems.msscpirateparrotproduct.dto.MessageDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.UomDTO;
import io.factorialsystems.msscpirateparrotproduct.service.UomService;
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
@RequestMapping("/api/v1/product/uom")
public class UomController {
    private final UomService uomService;

    @GetMapping
    public PagedDTO<UomDTO> findAll(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return uomService.findAll(pageNumber, pageSize);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findByName(@PathVariable("name") String name) {
        Optional<UomDTO> dto = uomService.findByName(name);

        if (dto.isEmpty()) {
            final String errorMessage = String.format("Unit of Measure with Name: %s Not Found", name);
            return ResponseEntity.badRequest().body(new MessageDTO(HttpStatus.BAD_REQUEST.value(), errorMessage));
        }

        return ResponseEntity.ok(dto.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) {
        Optional<UomDTO> dto = uomService.findById(id);

        if (dto.isEmpty()) {
            final String errorMessage = String.format("Unit of Measure with Id: %s Not Found", id);
            return ResponseEntity.badRequest().body(new MessageDTO(HttpStatus.BAD_REQUEST.value(), errorMessage));
        }

        return ResponseEntity.ok(dto.get());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody UomDTO dto) {
        uomService.clientSave(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(@PathVariable("id") String id, @Valid @RequestBody UomDTO dto) {
        uomService.clientUpdate(id, dto);
    }

    @PutMapping("/suspend/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void suspend(@PathVariable("id") String id) {
        uomService.clientSuspend(id);
    }

    @PutMapping("/unsuspend/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsuspend(@PathVariable("id") String id) {
        uomService.clientUnsuspend(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        uomService.clientDelete(id);
    }

    @GetMapping("/search/{search}")
    @ResponseStatus(HttpStatus.OK)
    public PagedDTO<UomDTO> search(@PathVariable("search") String search,
                                        @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return uomService.search(pageNumber, pageSize, search);
    }
}
