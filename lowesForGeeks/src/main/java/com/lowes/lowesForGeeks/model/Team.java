package com.lowes.lowesForGeeks.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
    @Id
    @Column(name="teamId")
    private Integer teamId;

    @NotBlank
    @Column(name="team_name")
    @Size(min = 0, max = 100)
    private String teamName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "teamId", referencedColumnName = "id")
    private List<Member> memberList;

    @Autowired
    public Team()
    {
        memberList=new ArrayList<Member>();
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
