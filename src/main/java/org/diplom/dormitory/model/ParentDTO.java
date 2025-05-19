package org.diplom.dormitory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentDTO {
    private Integer id;
    private String firstName;
    private String secondName;
    private String lastName;
    private String phoneNumber;
    private String mail;
    private String telegramId;
    private String chatId;
    private Integer roleId;
}
