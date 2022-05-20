package com.prgrms.board.service;

import com.prgrms.board.domain.User;
import com.prgrms.board.exception.NotFoundException;
import com.prgrms.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long registerUser(String name, int age, String hobby) {
        User savedUser = userRepository.save(User.of(name, age, hobby));
        return savedUser.getId();
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 userId입니다.")
        );
    }
}
