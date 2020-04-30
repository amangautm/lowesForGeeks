package com.lowes.lowesForGeeks.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Organization {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @Column(name = "organizationId")
  private Integer organizationId;
  @NotBlank
  @Column(name = "organizationName")
  private String organizationName;

  @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
  private List<Member> memberList;

  public Organization()
  {
    memberList=new ArrayList<>();
  }

  public Integer getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(int organizationId) {
    this.organizationId = organizationId;
  }

  public String getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  public List<Member> getMemberList() {
    return memberList;
  }

  public void setMemberList(List<Member> memberList) {
    this.memberList = memberList;
  }
}
