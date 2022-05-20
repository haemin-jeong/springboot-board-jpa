package com.prgrms.board.service;

import com.prgrms.board.domain.User;

public interface UserService {

    Long registerUser(String name, int age, String hobby);

    User findUserById(Long userId);
}
