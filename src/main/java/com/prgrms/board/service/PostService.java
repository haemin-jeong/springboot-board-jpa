package com.prgrms.board.service;

import com.prgrms.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Long addPost(Long userId, String title, String content);

    void updatePost(Long postId, String title, String content);

    Page<Post> findPosts(Pageable pageable);

    Post findPostById(Long postId);
}
