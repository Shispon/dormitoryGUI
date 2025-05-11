package org.diplom.dormitory.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParentModel {
    private Integer id;
    private String firstName;
    private String secondName;
    private String lastName;
    private String phoneNumber;
    private String mail;
    private String telegramId;
    private Integer roleId;
}
