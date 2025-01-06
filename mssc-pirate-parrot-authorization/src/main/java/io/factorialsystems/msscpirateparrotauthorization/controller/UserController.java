package io.factorialsystems.msscpirateparrotauthorization.controller;

import io.factorialsystems.msscpirateparrotauthorization.dto.ApplicationUserDTO;
import io.factorialsystems.msscpirateparrotauthorization.dto.PagedDTO;
import io.factorialsystems.msscpirateparrotauthorization.service.JpaUserDetailsService;
import io.factorialsystems.msscpirateparrotauthorization.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final JpaUserDetailsService userService;

    @GetMapping
    public ResponseEntity<PagedDTO<ApplicationUserDTO>> getUsers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = Constants.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }

        return ResponseEntity.ok(userService.loadUsers(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationUserDTO> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.loadUserById(id));
    }


}
