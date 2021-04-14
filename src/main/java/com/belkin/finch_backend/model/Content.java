package com.belkin.finch_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter @Setter
@Embeddable
@AllArgsConstructor @NoArgsConstructor
public class Content {

    @Column(columnDefinition = "VARCHAR")
    String type;

    @Column(columnDefinition = "VARCHAR")
    String value;
}
