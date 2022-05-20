package com.prgrms.board.service;

import com.prgrms.board.domain.Post;
import com.prgrms.board.domain.User;
import com.prgrms.board.exception.NotFoundException;
import com.prgrms.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Long addPost(Long userId, String title, String content) {
        User findUser = userService.findUserById(userId);
        Post savedPost = postRepository.save(Post.of(findUser, title, content));
        return savedPost.getId();
    }

    @Override
    @Transactional
    public void updatePost(Long postId, String title, String content) {
        Post findPost = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 postId입니다."));
        findPost.updatePost(title, content);
    }

    @Override
    public Page<Post> findPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 postId입니다."));
    }

}
