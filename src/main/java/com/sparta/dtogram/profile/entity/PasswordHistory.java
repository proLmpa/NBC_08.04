package com.sparta.dtogram.profile.entity;

import com.sparta.dtogram.common.entity.Timestamped;
import com.sparta.dtogram.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Password_History")
public class PasswordHistory extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public PasswordHistory(String password, User user) {
        this.password = password;
        this.user = user;
    }
}
