package com.prgrms.board.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class UserCreateRequest {

    @NotBlank
    @Length(max = 50)
    private String name;

    @NotNull
    private Integer age;

    private String hobby;
}
