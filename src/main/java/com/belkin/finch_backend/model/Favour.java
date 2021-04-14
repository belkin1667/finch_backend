package com.belkin.finch_backend.model;

import com.belkin.finch_backend.util.Base62;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "guide_favours")
@AllArgsConstructor
@NoArgsConstructor
public class Favour implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column(columnDefinition = "VARCHAR")
    String username;

    @Column(columnDefinition = "VARCHAR")
    String guide;

    public Base62 getGuideId() {
        return new Base62(guide);
    }

    public Favour(String username, Base62 guideId) {
        this.username = username;
        this.guide = guideId.getId();
    }

    @Override
    public int hashCode() {
        return (username + guide).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Favour) {
            Favour other = (Favour) obj;
            if (this.hashCode() == obj.hashCode())
                return username.equals(other.getUsername()) && guide.equals(other.getGuideId().getId());
        }
        return false;
    }
}
