package com.prgrms.board.controller;

import com.prgrms.board.controller.dto.PostRequest;
import com.prgrms.board.controller.dto.PostResponse;
import com.prgrms.board.controller.dto.PostUpdateRequest;
import com.prgrms.board.controller.dto.SaveResponse;
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
    private final PostConverter postConverter;

    @GetMapping(value = "/posts/v1", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<PostResponse>> list(Pageable pageable) {
        Page<Post> posts = postService.findPosts(pageable);
        return ResponseEntity.ok(postConverter.convertToPostResponsePage(posts));
    }

    @GetMapping(value = "/posts/v1/{postId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> findById(@PathVariable Long postId) {
        Post findPost = postService.findPostById(postId);
        PostResponse postResponse = postConverter.convertToPostResponse(findPost);
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping(value = "/posts/v1", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SaveResponse> save(@Valid @RequestBody PostRequest request) {
        Long savedPostId = postService.addPost(request.getUserId(), request.getTitle(), request.getContent());
        return new ResponseEntity<>(postConverter.convertToSaveResponse(savedPostId), HttpStatus.CREATED);
    }

    @PostMapping(value = "/posts/v1/{postId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SaveResponse> update(@Valid @RequestBody PostUpdateRequest updateRequest, @PathVariable Long postId) {
        postService.updatePost(postId,  updateRequest.getTitle(), updateRequest.getContent());
        return ResponseEntity.ok(postConverter.convertToSaveResponse(postId));
    }
}
