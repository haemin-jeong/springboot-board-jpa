package com.prgrms.board.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 5000, nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Post(User user, String title, String content) {
        super(LocalDateTime.now(), user.getName());
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static Post of(User user, String title, String content) {
        return new Post(user, title, content);
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
