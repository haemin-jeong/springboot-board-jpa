package com.prgrms.board.controller;

import com.prgrms.board.controller.dto.*;
import com.prgrms.board.converter.PostConverter;
import com.prgrms.board.domain.Post;
import com.prgrms.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;

    @GetMapping(value = "/posts/v1", produces = APPLICATION_JSON_VALUE)
    public ApiResponse<PostListResponse> list(Pageable pageable) {
        Page<Post> posts = postService.findPosts(pageable);
        return ApiResponse.of(SC_OK, PostConverter.convertToPostResponsePage(posts));
    }

    @GetMapping(value = "/posts/v1/{postId}", produces = APPLICATION_JSON_VALUE)
    public ApiResponse<PostResponse> findById(@PathVariable Long postId) {
        Post findPost = postService.findPostById(postId);
        return ApiResponse.of(SC_OK, PostConverter.convertToPostResponse(findPost));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/posts/v1", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ApiResponse<SaveResponse> save(@RequestBody @Valid PostRequest request) {
        Long savedPostId = postService.addPost(request.getUserId(), request.getTitle(), request.getContent());
        return ApiResponse.of(SC_CREATED, PostConverter.convertToSaveResponse(savedPostId));
    }

    @PostMapping(value = "/posts/v1/{postId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ApiResponse<SaveResponse> update(@PathVariable Long postId, @RequestBody @Valid PostUpdateRequest updateRequest) {
        postService.updatePost(postId,  updateRequest.getTitle(), updateRequest.getContent());
        return ApiResponse.of(SC_OK, PostConverter.convertToSaveResponse(postId));
    }
}
