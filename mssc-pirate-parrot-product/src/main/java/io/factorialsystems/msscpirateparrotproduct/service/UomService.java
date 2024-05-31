package io.factorialsystems.msscpirateparrotproduct.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.factorialsystems.msscpirateparrotproduct.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotproduct.dto.UomDTO;
import io.factorialsystems.msscpirateparrotproduct.model.Uom;
import io.factorialsystems.msscpirateparrotproduct.repository.UomRepository;
import io.factorialsystems.msscpirateparrotproduct.security.JwtTokenWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UomService {
    private final UomRepository uomRepository;

    public void clientSave(UomDTO dto) {
        Uom uom = Uom.createUom(dto.getName());
        log.info("Saving Unit of Measure {}", uom);
        uomRepository.save(uom);
    }

    public void clientUpdate (String id, UomDTO dto) {
        dto.setId(id);
        Uom uom = Uom.createUomFromDto(dto);
        log.info("updating Unit of Measure {}", uom);
        uomRepository.update(uom);
    }

    public PagedDTO<UomDTO> findAll(Integer pageNumber, Integer pageSize) {
        log.info("Find All Product Variants PageNumber : {}, PageSize {}", pageNumber, pageSize);

        try (var ignored = PageHelper.startPage(pageNumber, pageSize)) {
            return createClientDto(uomRepository.findAll());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public Optional<UomDTO> findById(String id) {
        log.info("Find Unit of Measure by Id {}", id);
        Uom uom = uomRepository.findById(id);
        return (uom == null) ? Optional.empty() : Optional.of(uom.createDto());
    }

    public Optional<UomDTO> findByName(String name) {
        log.info("Find Unit of Measure by Name {}", name);
        Uom uom = uomRepository.findByName(name);
        return (uom == null) ? Optional.empty() : Optional.of(uom.createDto());
    }

    public PagedDTO<UomDTO> search(Integer pageNumber, Integer pageSize, String searchString) {
        log.info("Search Unit of Measure PageNumber {}, PageSize {}, search String {}", pageNumber, pageSize, searchString);

        try (var ignored = PageHelper.startPage(pageNumber, pageSize)) {
            return createClientDto(uomRepository.search(searchString));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void clientDelete(String id) {
        log.info("Delete Unit of Measure Variant {}, deleted By {}", id, JwtTokenWrapper.getUserName());
    }

    public void clientSuspend(String id) {
        log.info("Suspend Unit of Measure {}", id);
        uomRepository.suspend(id);
    }

    public void clientUnsuspend(String id) {
        log.info("Unsuspend Unit of Measure {}", id);
        uomRepository.unsuspend(id);
    }

    private PagedDTO<UomDTO> createClientDto(Page<Uom> uoms) {
        List<Uom> result = uoms.getResult();

        PagedDTO<UomDTO> pagedDto = new PagedDTO<>();
        pagedDto.setTotalSize((int) uoms.getTotal());
        pagedDto.setPageNumber(uoms.getPageNum());
        pagedDto.setPageSize(uoms.getPageSize());
        pagedDto.setPages(uoms.getPages());
        pagedDto.setList(result.stream().map(Uom::createDto).toList());

        return pagedDto;
    }
}
