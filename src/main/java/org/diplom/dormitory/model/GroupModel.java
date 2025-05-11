package org.diplom.dormitory.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupModel {
    private Integer id;
    private String groupName;
    private Integer curatorId;



    @JsonCreator
    public GroupModel(@JsonProperty("id") int id, @JsonProperty("groupName") String name , @JsonProperty("curator_id") Integer curatorId) {
        this.id = id;
        this.groupName = name;
        this.curatorId = curatorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getCuratorId() {
        return curatorId;
    }

    public void setCuratorId(Integer curatorId) {
        this.curatorId = curatorId;
    }

    @Override
    public String toString() {
        return groupName;
    }
}
