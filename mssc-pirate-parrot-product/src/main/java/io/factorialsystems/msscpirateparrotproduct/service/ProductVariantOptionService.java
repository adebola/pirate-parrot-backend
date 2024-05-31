package io.factorialsystems.msscpirateparrotproduct.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.ProductVariantOptionDTO;
import io.factorialsystems.msscpirateparrotproduct.model.ProductVariantOption;
import io.factorialsystems.msscpirateparrotproduct.repository.ProductVariantOptionRepository;
import io.factorialsystems.msscpirateparrotproduct.security.JwtTokenWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductVariantOptionService {
    private final ProductVariantOptionRepository productVariantOptionRepository;

    public void clientSave(ProductVariantOptionDTO dto) {
        ProductVariantOption pvo = ProductVariantOption.createProductVariant(dto.getName());
        log.info("Saving Product Variant Option {}", pvo);
        productVariantOptionRepository.save(pvo);
    }

    public void clientUpdate (String id, ProductVariantOptionDTO dto) {
        dto.setId(id);
        ProductVariantOption pvo = ProductVariantOption.createProductVariantFromDto(dto);
        log.info("updating Product Variant Option {}", pvo);
        productVariantOptionRepository.update(pvo);
    }

    public PagedDTO<ProductVariantOptionDTO> findAll(Integer pageNumber, Integer pageSize) {
        log.info("Find All Product Variants Options PageNumber : {}, PageSize {}", pageNumber, pageSize);

        try (var ignored = PageHelper.startPage(pageNumber, pageSize)) {
            return createClientDto(productVariantOptionRepository.findAll());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Optional<ProductVariantOptionDTO> findById(String id) {
        log.info("Find Product Variant Option by Id {}", id);
        ProductVariantOption pvo = productVariantOptionRepository.findById(id);
        return (pvo == null) ? Optional.empty() : Optional.of(pvo.createDto());
    }

    public Optional<ProductVariantOptionDTO> findByName(String name) {
        log.info("Find Product Variant Option by Name {}", name);
        ProductVariantOption pvo = productVariantOptionRepository.findByName(name);
        return (pvo == null) ? Optional.empty() : Optional.of(pvo.createDto());
    }

    public PagedDTO<ProductVariantOptionDTO> search(Integer pageNumber, Integer pageSize, String searchString) {
        log.info("Search Product Variant Option PageNumber {}, PageSize {}, search String {}", pageNumber, pageSize, searchString);

        try (var ignored = PageHelper.startPage(pageNumber, pageSize)) {
            return createClientDto(productVariantOptionRepository.search(searchString));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void clientDelete(String id) {
        log.info("Delete Product Variant Option {}, deleted By {}", id, JwtTokenWrapper.getUserName());
    }

    public void clientSuspend(String id) {
        log.info("Suspend Product Variant Option {}", id);
        productVariantOptionRepository.suspend(id);
    }

    public void clientUnsuspend(String id) {
        log.info("Unsuspend Product Variant Option {}", id);
        productVariantOptionRepository.unsuspend(id);
    }

    private PagedDTO<ProductVariantOptionDTO> createClientDto(Page<ProductVariantOption> productVariants) {
        List<ProductVariantOption> result = productVariants.getResult();

        PagedDTO<ProductVariantOptionDTO> pagedDto = new PagedDTO<>();
        pagedDto.setTotalSize((int) productVariants.getTotal());
        pagedDto.setPageNumber(productVariants.getPageNum());
        pagedDto.setPageSize(productVariants.getPageSize());
        pagedDto.setPages(productVariants.getPages());
        pagedDto.setList(result.stream().map(ProductVariantOption::createDto).toList());

        return pagedDto;
    }
}
