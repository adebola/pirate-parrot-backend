package io.factorialsystems.msscpirateparrotproduct.model;

import io.factorialsystems.msscpirateparrotproduct.dto.ProductVariantOptionDTO;
import io.factorialsystems.msscpirateparrotproduct.security.JwtTokenWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@ToString
@Getter
@NoArgsConstructor
public class ProductVariantOption {
    private String id;
    private String name;
    private Instant createdOn;
    private String createdBy;
    private Boolean suspended;

    static public ProductVariantOption createProductVariant(String name) {
        ProductVariantOption pvo = new ProductVariantOption();
        pvo.name = name;
        pvo.createdBy = JwtTokenWrapper.getUserName();

        return pvo;
    }

    static public ProductVariantOption createProductVariantFromDto(ProductVariantOptionDTO dto) {
        ProductVariantOption pvo = new ProductVariantOption();
        pvo.id = dto.getId();
        pvo.name = dto.getName();

        return pvo;
    }

    public ProductVariantOptionDTO createDto() {
        ProductVariantOptionDTO dto = new ProductVariantOptionDTO();
        dto.setId(this.id);
        dto.setName(this.name);

        return dto;
    }
}
