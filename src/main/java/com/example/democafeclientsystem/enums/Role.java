package com.example.democafeclientsystem.enums;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    private int id;
    @Column
    private String name;
}
