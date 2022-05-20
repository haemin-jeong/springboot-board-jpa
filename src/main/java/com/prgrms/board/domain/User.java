package com.prgrms.board.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column
    private String hobby;

    private User(String name, int age, String hobby) {
        super(LocalDateTime.now(), name);
        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }

    public static User of(String name, int age, String hobby) {
        return new User(name, age, hobby);
    }
}
