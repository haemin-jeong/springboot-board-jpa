package com.prgrms.board.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class PostRequest {

    @NotBlank
    @Length(max = 100)
    private String title;

    @NotBlank
    @Length(max = 5000)
    private String content;

    @NotNull
    private Long userId;
}
