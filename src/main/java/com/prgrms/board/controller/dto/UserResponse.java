package com.prgrms.board.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {

    private Long id;
    private String name;
    private Integer age;
    private String hobby;
}
