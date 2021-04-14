package com.belkin.finch_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@Entity
@AllArgsConstructor @NoArgsConstructor
public class Subscription implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public Subscription(String username, String subscriber) {
        this.username = username;
        this.subscriber = subscriber;
    }

    @Column(columnDefinition = "VARCHAR")
    private String username;

    @Column(columnDefinition = "VARCHAR")
    private String subscriber;


    @Override
    public int hashCode() {
        return (username + subscriber).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Subscription) {
            Subscription other = (Subscription) obj;
            if (this.hashCode() == other.hashCode())
                return username.equals(other.getUsername()) && subscriber.equals(other.getSubscriber());
        }
        return false;
    }
}
