package com.prgrms.board.service;

import com.prgrms.board.domain.User;
import com.prgrms.board.exception.NotFoundException;
import com.prgrms.board.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("user 회원가입 테스트")
    void saveTest() {
        // Given
        User user = createUser(1L);
        when(userRepository.save(any())).thenReturn(user);

        // When
        Long savedUserId = userService.registerUser(user.getName(), user.getAge(), user.getHobby());

        // Then
        verify(userRepository).save(any());
        assertThat(savedUserId).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("ID로 User를 조회한다.")
    void findByIdTest() {
        // Given
        User user = createUser(2L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When
        User findUser = userService.findUserById(user.getId());

        // Then
        verify(userRepository).findById(user.getId());
        assertThat(findUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    @DisplayName("존재하지 않는 userId로 User를 조회하면 NotFoundException이 발생한다.")
    void findByInvalidIdTest() {
        // Given
        Long invalidId = -1L;
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> userService.findUserById(invalidId))
                .isExactlyInstanceOf(NotFoundException.class);
        verify(userRepository).findById(invalidId);
    }

    private User createUser(Long userId) {
        User user = User.of("name", 24, "soccer");
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }
}