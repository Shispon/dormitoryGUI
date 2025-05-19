package org.diplom.dormitory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResidentDTO {
    private Integer id;
    private String firstName;
    private String secondName;
    private String lastName;
    private LocalDate age;
    private String phoneNumber;
    private String mail;
    private String telegramId;
    private String chatId;
    private byte[] photo;
    private byte[] qrCode;
    private Boolean isPresent;
    private LocalDateTime dateCreated;
    private LocalDateTime dateDeleted;
    private LocalDateTime datePresent;
    private LocalDateTime dateMissing;
    private Integer roleId;
    private Integer groupId;
}
