package com.ghd.kg.ghd.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "chats", schema = "tg")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TgChats {
    @Id
    Long id;
    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;

    String username;
    String description;
    String title;
    String type;

    @Column(name = "is_main")
    boolean isMain;
}
