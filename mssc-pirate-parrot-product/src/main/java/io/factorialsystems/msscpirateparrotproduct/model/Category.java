package io.factorialsystems.msscpirateparrotproduct.model;

import io.factorialsystems.msscpirateparrotproduct.dto.CategoryDTO;
import io.factorialsystems.msscpirateparrotproduct.security.JwtTokenWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@ToString
@Getter
@NoArgsConstructor
public class Category {
    private String id;
    private String name;
    private String imageUrl;
    private Instant createdOn;
    private String createdBy;
    private Boolean suspended;

    static public Category createCategory(String name, String imageUrl) {
        Category c = new Category();
        c.name = name;
        c.imageUrl = imageUrl;
        c.createdBy = JwtTokenWrapper.getUserName();
        return c;
    }

    static public Category createCategoryFromDto(CategoryDTO dto) {
        Category c = new Category();
        c.id = dto.getId();
        c.name = dto.getName();
        c.imageUrl = dto.getImageUrl();

        return c;
    }

    public CategoryDTO createDto() {
        return new CategoryDTO(id, name, imageUrl);
    }
}
