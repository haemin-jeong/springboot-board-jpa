package com.prgrms.board.controller;

import com.prgrms.board.controller.dto.ApiResponse;
import com.prgrms.board.controller.dto.SaveResponse;
import com.prgrms.board.controller.dto.UserCreateRequest;
import com.prgrms.board.controller.dto.UserResponse;
import com.prgrms.board.converter.UserConverter;
import com.prgrms.board.domain.User;
import com.prgrms.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/users/v1", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ApiResponse<SaveResponse> save(@RequestBody @Valid UserCreateRequest request) {
        Long savedId = userService.registerUser(request.getName(), request.getAge(), request.getHobby());
        return ApiResponse.of(SC_CREATED, UserConverter.convertToSaveResponse(savedId));
    }

    @GetMapping(value = "/users/v1/{userId}", produces = APPLICATION_JSON_VALUE)
    public ApiResponse<UserResponse> findById(@PathVariable Long userId) {
        User findUser = userService.findUserById(userId);
        return ApiResponse.of(SC_OK, UserConverter.convertToUserResponse(findUser));
    }

}
