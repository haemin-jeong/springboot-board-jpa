package com.prgrms.board.converter;

import com.prgrms.board.controller.dto.PostResponse;
import com.prgrms.board.controller.dto.SaveResponse;
import com.prgrms.board.controller.dto.UserResponse;
import com.prgrms.board.domain.Post;
import com.prgrms.board.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

    public SaveResponse convertToSaveResponse(Long postId) {
        return new SaveResponse(postId);
    }

    public Page<PostResponse> convertToPostResponsePage(Page<Post> posts) {
        return posts.map(this::convertToPostResponse);
    }

    public PostResponse convertToPostResponse(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), convertToUserResponse(post.getUser()));
    }

    private UserResponse convertToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getAge(), user.getHobby());
    }

}
