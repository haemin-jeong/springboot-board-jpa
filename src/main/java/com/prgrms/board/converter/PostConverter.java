package com.prgrms.board.converter;

import com.prgrms.board.controller.dto.*;
import com.prgrms.board.domain.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    public static SaveResponse convertToSaveResponse(Long postId) {
        return new SaveResponse(postId);
    }

    public static PostListResponse convertToPostResponsePage(Page<Post> page) {
        List<PostResponse> postResponses = page.getContent().stream()
                .map(PostConverter::convertToPostResponse)
                .collect(Collectors.toList());
        PageInfo pageInfo = PageInfo.from(page);
        return new PostListResponse(postResponses, pageInfo);
    }

    public static PostResponse convertToPostResponse(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), UserConverter.convertToUserResponse(post.getUser()));
    }

}
