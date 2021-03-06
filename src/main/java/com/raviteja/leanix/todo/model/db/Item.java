package com.raviteja.leanix.todo.model.db;

import lombok.Data;

import javax.persistence.*;


@Table
@Entity
@Data
public class Item extends AbstractBaseEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "POSITION")
    private int position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LIST_ID", nullable = false)
    private ListEntity listEntity;
}
