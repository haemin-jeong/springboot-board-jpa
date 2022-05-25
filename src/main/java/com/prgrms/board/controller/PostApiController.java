package com.prgrms.board.controller;

import com.prgrms.board.controller.dto.*;
import com.prgrms.board.converter.PostConverter;
import com.prgrms.board.domain.Post;
import com.prgrms.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;

    @GetMapping(value = "/posts/v1", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponse<PostResponse>> list(Pageable pageable) {
        Page<Post> posts = postService.findPosts(pageable);
        return ResponseEntity.ok(PostConverter.convertToPostResponsePage(posts));
    }

    @GetMapping(value = "/posts/v1/{postId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> findById(@PathVariable Long postId) {
        Post findPost = postService.findPostById(postId);
        PostResponse postResponse = PostConverter.convertToPostResponse(findPost);
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping(value = "/posts/v1", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SaveResponse> save(@RequestBody @Valid PostRequest request) {
        Long savedPostId = postService.addPost(request.getUserId(), request.getTitle(), request.getContent());
        return new ResponseEntity<>(PostConverter.convertToSaveResponse(savedPostId), HttpStatus.CREATED);
    }

    @PostMapping(value = "/posts/v1/{postId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SaveResponse> update(@PathVariable Long postId, @RequestBody @Valid PostUpdateRequest updateRequest) {
        postService.updatePost(postId,  updateRequest.getTitle(), updateRequest.getContent());
        return ResponseEntity.ok(PostConverter.convertToSaveResponse(postId));
    }
}
