package com.geoffwellington.manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "membership")
@EntityListeners(AuditingEntityListener.class)
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @CreatedDate
    @Column
    private Instant created;

    @LastModifiedDate
    @Column
    private Instant modified;

    @Column
    private String name;

    @ManyToMany
    @JoinTable(
            name = "group_membership",
            joinColumns = @JoinColumn(name = "membership_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<User> people;

    public Membership(UUID uuid, String name, Set<User> people) {
        this.uuid = uuid;
        this.name = name;
        this.people = people;
    }

    public Membership() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getPeople() {
        return people;
    }

    public void setPeople(Set<User> people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Membership that = (Membership) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name);
    }
}
