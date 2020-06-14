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

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        Integer userId = userCrudService.getUserIdFromSecurityContext();
        if (userId.equals(id) || userId == 1)
            return ResponseEntity.ok(userCrudService.findById(id));
        else
            return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<UserDto> userUpdate(@PathVariable Integer id, @RequestBody UserDto user) {
        user.setId(id);
        return ResponseEntity.ok(userCrudService.update(user));
    }

    @GetMapping("/list")
    public ResponseEntity<Set<UserDto>> userList(@PathVariable(name = "p") Optional<Integer> page) {
        return ResponseEntity.ok(userCrudService.findAll(page.orElse(0), 20));
    }

    @GetMapping("/search")
    public ResponseEntity<UserDto> userList(@PathVariable(name = "q") String email) {
        return ResponseEntity.ok(userCrudService.findByEmailPriv(email));
    }

    @PostMapping("/{id}/del")
    public ResponseEntity<String> userDelete(@PathVariable Integer id) {
        userCrudService.delete(id);
        return ResponseEntity.ok("done");
    }
}
