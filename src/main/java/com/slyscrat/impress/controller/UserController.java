package com.slyscrat.impress.controller;

import com.slyscrat.impress.model.dto.UserDto;
import com.slyscrat.impress.service.crud.UserCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserCrudService userCrudService;

    @PostMapping("/{id}/edit")
    public ResponseEntity<UserDto> userUpdate(@PathVariable Integer id, @RequestBody UserDto user) {
        user.setId(id);
        return ResponseEntity.ok(userCrudService.update(user));
    }

    @GetMapping("/list")
    public ResponseEntity<Set<UserDto>> userList(@PathVariable(name = "p") Optional<Integer> page) {
        return ResponseEntity.ok(userCrudService.findAll(page.orElse(0), 20));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> userDelete(@PathVariable Integer id) {
        userCrudService.delete(id);
        return ResponseEntity.ok("done");
    }
}
