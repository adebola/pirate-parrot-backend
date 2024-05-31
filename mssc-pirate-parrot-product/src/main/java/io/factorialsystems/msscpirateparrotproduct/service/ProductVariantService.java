package io.factorialsystems.msscpirateparrotproduct.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.ProductVariantDTO;
import io.factorialsystems.msscpirateparrotproduct.model.ProductVariant;
import io.factorialsystems.msscpirateparrotproduct.repository.ProductVariantRepository;
import io.factorialsystems.msscpirateparrotproduct.security.JwtTokenWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;

    public void clientSave(ProductVariantDTO dto) {
        ProductVariant pv = ProductVariant.createProductVariant(dto.getName());
        log.info("Saving Product Variant {}", pv);
        productVariantRepository.save(pv);
    }

    public void clientUpdate (String id, ProductVariantDTO dto) {
        dto.setId(id);
        ProductVariant pv = ProductVariant.createProductVariantFromDto(dto);
        log.info("updating Product Variant {}", pv);
        productVariantRepository.update(pv);
    }

    public PagedDTO<ProductVariantDTO> findAll(Integer pageNumber, Integer pageSize) {
        log.info("Find All Product Variants PageNumber : {}, PageSize {}", pageNumber, pageSize);

        try (var ignored = PageHelper.startPage(pageNumber, pageSize)) {
            return createClientDto(productVariantRepository.findAll());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Optional<ProductVariantDTO> findById(String id) {
        log.info("Find Product Variant by Id {}", id);
        ProductVariant pv = productVariantRepository.findById(id);
        return (pv == null) ? Optional.empty() : Optional.of(pv.createDto());
    }

    public Optional<ProductVariantDTO> findByName(String name) {
        log.info("Find Product Variant by Name {}", name);
        ProductVariant pv = productVariantRepository.findByName(name);
        return (pv == null) ? Optional.empty() : Optional.of(pv.createDto());
    }

    public PagedDTO<ProductVariantDTO> search(Integer pageNumber, Integer pageSize, String searchString) {
        log.info("Search Product Variant PageNumber {}, PageSize {}, search String {}", pageNumber, pageSize, searchString);

        try (var ignored = PageHelper.startPage(pageNumber, pageSize)) {
            return createClientDto(productVariantRepository.search(searchString));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void clientDelete(String id) {
        log.info("Delete Product Variant {}, deleted By {}", id, JwtTokenWrapper.getUserName());
    }

    public void clientSuspend(String id) {
        log.info("Suspend Product Variant {}", id);
        productVariantRepository.suspend(id);
    }

    public void clientUnsuspend(String id) {
        log.info("Unsuspend Product Variant {}", id);
        productVariantRepository.unsuspend(id);
    }

    private PagedDTO<ProductVariantDTO> createClientDto(Page<ProductVariant> productVariants) {
        List<ProductVariant> result = productVariants.getResult();

        PagedDTO<ProductVariantDTO> pagedDto = new PagedDTO<>();
        pagedDto.setTotalSize((int) productVariants.getTotal());
        pagedDto.setPageNumber(productVariants.getPageNum());
        pagedDto.setPageSize(productVariants.getPageSize());
        pagedDto.setPages(productVariants.getPages());
        pagedDto.setList(result.stream().map(ProductVariant::createDto).toList());

        return pagedDto;
    }
}
