package com.prgrms.board.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PageResponse<T> {

    private List<T> contents;
    private PageInfo pageInfo;
}
