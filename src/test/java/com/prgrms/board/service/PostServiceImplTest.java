package com.prgrms.board.service;

import com.prgrms.board.domain.Post;
import com.prgrms.board.domain.User;
import com.prgrms.board.exception.NotFoundException;
import com.prgrms.board.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    PostRepository postRepository;

    @Mock
    UserService userService;

    @InjectMocks
    PostServiceImpl postService;

    private User createUser(Long userId) {
        User user = User.of("sample", 25, "soccer");
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }

    private Post createPost(Long postId, User user) {
        Post post = Post.of(user, "title", "content");
        ReflectionTestUtils.setField(post, "id", postId);
        return post;
    }

    @Test
    @DisplayName("게시글을 저장한다.")
    void addPostTest() {
        // Given
        User user = createUser(1L);
        when(userService.findUserById(user.getId())).thenReturn(user);

        Post post = createPost(1L, user);
        when(postRepository.save(any())).thenReturn(post);

        // When
        Long savedId = postService.addPost(user.getId(), post.getTitle(), post.getContent());

        // Then
        verify(postRepository).save(any());
        verify(userService).findUserById(user.getId());
        assertThat(savedId).isEqualTo(post.getId());
    }

    @Test
    @DisplayName("게시글을 수정한다.")
    void updatePostTest() {
        // Given
        User user = createUser(1L);
        Post post = createPost(2L, user);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        // When
        String updateTitle = "updateTitle";
        String updateContent = "updateContent";
        postService.updatePost(post.getId(), updateTitle, updateContent);

        // Then
        verify(postRepository).findById(post.getId());
        assertThat(post.getTitle()).isEqualTo(updateTitle);
        assertThat(post.getContent()).isEqualTo(updateContent);
    }

    @Test
    @DisplayName("존재하지 않는 postId의 게시글을 수정하려하면 IllegalArgumentException 예외가 발생한다.")
    void updatePostInvalidPostIdTest() {
        // Given
        Long invalidPostId = -1L;
        when(postRepository.findById(invalidPostId)).thenReturn(Optional.empty());

        // When //Then
        assertThatThrownBy(() -> postService.updatePost(invalidPostId, "updateTitle", "updateContent"))
                .isExactlyInstanceOf(NotFoundException.class);
        verify(postRepository).findById(invalidPostId);
    }

    //TODO findPosts 테스트


    @Test
    @DisplayName("ID로 Post를 조회한다.")
    void findPostByIdTest() {
        // Given
        User user = createUser(1L);
        Post post = createPost(1L, user);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        // When
        Post findPost = postService.findPostById(post.getId());

        // Then
        verify(postRepository).findById(post.getId());
        assertThat(findPost).usingRecursiveComparison().isEqualTo(post);
    }

    @Test
    @DisplayName("존재하지 않는 postId로 Post를 조회하면 IllegalArgumentException이 발생한다.")
    void findPostByInvalidIdTest() {
        // Given
        Long invalidPostId = -1L;
        when(postRepository.findById(invalidPostId)).thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> postService.findPostById(invalidPostId))
                .isExactlyInstanceOf(NotFoundException.class);
        verify(postRepository).findById(invalidPostId);
    }
}