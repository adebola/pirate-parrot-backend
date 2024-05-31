package io.factorialsystems.msscpirateparrotproduct.model;

import io.factorialsystems.msscpirateparrotproduct.dto.UomDTO;
import io.factorialsystems.msscpirateparrotproduct.security.JwtTokenWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@ToString
@Getter
@NoArgsConstructor
public class Uom {
    private String id;
    private String name;
    private Instant createdOn;
    private String createdBy;
    private Boolean suspended;

    static public Uom createUom(String name) {
        Uom uom = new Uom();
        uom.name = name;
        uom.createdBy = JwtTokenWrapper.getUserName();

        return uom;
    }

    static public Uom createUomFromDto(UomDTO dto) {
        Uom uom = new Uom();
        uom.id = dto.getId();
        uom.name = dto.getName();

        return uom;
    }

    public UomDTO createDto() {
        return new UomDTO(this.id, this.name);
    }
}
