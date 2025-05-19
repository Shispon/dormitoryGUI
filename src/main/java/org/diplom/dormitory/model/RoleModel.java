package org.diplom.dormitory.model;

import lombok.Data;

@Data
public class RoleModel {
    private Integer id;
    private String roleName;

    @Override
    public String toString() {
        return
                roleName;

    }
}
