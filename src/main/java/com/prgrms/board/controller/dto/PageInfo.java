package com.prgrms.board.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PageInfo {
    private Long totalElementCount;
    private Integer totalPageCount;
    private Integer pageNumber;
    private Integer size;
    private Boolean isFirstPage;
    private Boolean hasNextPage;
    private Boolean hasPrevPage;

    public static PageInfo from(Page page) {
        return new PageInfo(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
