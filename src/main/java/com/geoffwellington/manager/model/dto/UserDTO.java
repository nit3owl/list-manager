package com.geoffwellington.manager.model.dto;

import com.geoffwellington.manager.model.ManagedList;
import com.geoffwellington.manager.model.Membership;
import com.geoffwellington.manager.model.SubscriptionType;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class UserDTO {
    private UUID id;

    private String email;

    private Instant created;

    private Instant modified;

    private String firstName;

    private String lastName;

    private SubscriptionType subscriptionType;

    private Set<Membership> memberships;

    private Set<ManagedList> lists;

    public UserDTO() {
    }

    public UserDTO(UUID id, String email, Instant created, Instant modified, String firstName, String lastName,
                   SubscriptionType subscriptionType, Set<Membership> memberships, Set<ManagedList> lists) {
        this.id = id;
        this.email = email;
        this.created = created;
        this.modified = modified;
        this.firstName = firstName;
        this.lastName = lastName;
        this.subscriptionType = subscriptionType;
        this.memberships = memberships;
        this.lists = lists;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
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

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Set<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<Membership> memberships) {
        this.memberships = memberships;
    }

    public Set<ManagedList> getLists() {
        return lists;
    }

    public void setLists(Set<ManagedList> lists) {
        this.lists = lists;
    }
}
