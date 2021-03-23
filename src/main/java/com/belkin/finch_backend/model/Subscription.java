package com.belkin.finch_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class Subscription {

    private String username;
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
