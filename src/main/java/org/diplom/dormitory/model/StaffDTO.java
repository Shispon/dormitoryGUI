package org.diplom.dormitory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
    private Integer id;
    private String firstName;
    private String secondName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private String email;
    private Integer roleId;
}
