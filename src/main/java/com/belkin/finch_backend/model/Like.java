package com.belkin.finch_backend.model;

import com.belkin.finch_backend.util.Base62;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class Like {

    String username;
    Base62 guideId;

    @Override
    public int hashCode() {
        return (username + guideId.getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Like) {
            Like other = (Like) obj;
            if (this.hashCode() == obj.hashCode())
                return username.equals(other.getUsername()) && guideId.equals(other.getGuideId());
        }
        return false;
    }
}
