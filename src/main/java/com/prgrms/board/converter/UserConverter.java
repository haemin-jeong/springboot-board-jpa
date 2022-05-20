package com.prgrms.board.converter;

import com.prgrms.board.controller.dto.SaveResponse;
import com.prgrms.board.controller.dto.UserResponse;
import com.prgrms.board.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public SaveResponse convertToSaveResponse(Long userId) {
        return new SaveResponse(userId);
    }

    public UserResponse convertToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getAge(), user.getHobby());
    }
}
