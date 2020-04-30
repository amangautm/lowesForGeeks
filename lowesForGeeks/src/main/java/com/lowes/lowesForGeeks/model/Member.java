package com.lowes.lowesForGeeks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @Column(name = "firstName")
    @Size(min = 0, max = 100)
    private String firstName;

    @Column(name = "lastName")
    @Size(min = 0, max = 100)
    private String lastName;

    @NotBlank
    @Column(name = "email")
    private String email;

    @JsonIgnore(value = false)
    @Column(name = "teamAdmin")
    private boolean teamAdmin;

    @JsonIgnore(value = false)
    @Column(name = "organizationAdmin")
    private boolean organizationAdmin;

    @Column(name = "teamId")
    private Integer teamId;

    @Column(name = "organizationId")
    private Integer organizationId;

    @JsonIgnore
    @ManyToMany
    private List<Event> events;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOrganizationAdmin() {
        return organizationAdmin;
    }

    public void setOrganizationAdmin(boolean organizationAdmin) {
        this.organizationAdmin = organizationAdmin;
    }

    public boolean isTeamAdmin() {
        return teamAdmin;
    }

    public void setTeamAdmin(boolean teamAdmin) { this.teamAdmin = teamAdmin; }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
}