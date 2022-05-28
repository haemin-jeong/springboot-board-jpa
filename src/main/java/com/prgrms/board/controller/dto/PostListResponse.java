package com.prgrms.board.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostListResponse {

    private List<PostResponse> contents;
    private PageInfo pageInfo;
}
