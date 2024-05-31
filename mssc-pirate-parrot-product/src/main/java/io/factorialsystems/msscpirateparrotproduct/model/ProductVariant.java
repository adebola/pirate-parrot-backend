package io.factorialsystems.msscpirateparrotproduct.model;

import io.factorialsystems.msscpirateparrotproduct.dto.ProductVariantDTO;
import io.factorialsystems.msscpirateparrotproduct.security.JwtTokenWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@ToString
@Getter
@NoArgsConstructor
public class ProductVariant {
    private String id;
    private String name;
    private Instant createdOn;
    private String createdBy;
    private Boolean suspended;

    static public ProductVariant createProductVariant(String name) {
       ProductVariant pv = new ProductVariant();
       pv.name = name;
       pv.createdBy = JwtTokenWrapper.getUserName();

       return pv;
    }

    static public ProductVariant createProductVariantFromDto(ProductVariantDTO dto) {
        ProductVariant pv = new ProductVariant();
        pv.id = dto.getId();
        pv.name = dto.getName();

        return pv;
    }

    public ProductVariantDTO createDto() {
        ProductVariantDTO dto = new ProductVariantDTO();
        dto.setId(this.id);
        dto.setName(this.name);

        return dto;
    }
}
