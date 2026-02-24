package com.thc.capchatbot.domain;

import com.thc.capchatbot.dto.DefaultDto;
import com.thc.capchatbot.dto.GroupDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "tb_group")
public class Group extends AuditingFields {
    @Setter
    String groupName;

    protected Group() {}
    private Group(String groupName) {
        this.groupName = groupName;
    }

    public static Group of (String groupName) {
        return new Group(groupName);
    }

    public void update(GroupDto.UpdateReqDto param){
        if(param.getDeleted() != null){
            setDeleted(param.getDeleted());
        }
        if(param.getGroupName() != null){
            setGroupName(param.getGroupName());
        }
    }

    public DefaultDto.CreateResDto toCreateResDto(){
        return DefaultDto.CreateResDto.builder()
                .id(getId())
                .build();
    }
}
