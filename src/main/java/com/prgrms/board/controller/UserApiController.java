package com.prgrms.board.controller;

import com.prgrms.board.controller.dto.SaveResponse;
import com.prgrms.board.controller.dto.UserCreateRequest;
import com.prgrms.board.controller.dto.UserResponse;
import com.prgrms.board.converter.UserConverter;
import com.prgrms.board.domain.User;
import com.prgrms.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping(value = "/users/v1", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SaveResponse> save(@Valid @RequestBody UserCreateRequest request) {
        Long savedId = userService.registerUser(request.getName(), request.getAge(), request.getHobby());
        return new ResponseEntity<>(UserConverter.convertToSaveResponse(savedId), HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/v1/{userId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> findById(@PathVariable Long userId) {
        User findUser = userService.findUserById(userId);
        return ResponseEntity.ok(UserConverter.convertToUserResponse(findUser));
    }
}
