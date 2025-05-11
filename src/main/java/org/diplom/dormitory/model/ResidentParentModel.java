package org.diplom.dormitory.model;

import lombok.Data;

@Data
public class ResidentParentModel {
    private Integer id;
    private Integer parentId;
    private Integer residentId;
}
