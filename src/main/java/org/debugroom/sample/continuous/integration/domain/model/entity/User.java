package org.debugroom.sample.continuous.integration.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "USR")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {

    @Id
    @Column(name="ID")
    private Long id;
    @Column(name="FIRST_NAME")
    private String firstName;
    @Column(name="FAMILY_NAME")
    private String familyName;
    @Column(name="VERSION")
    private int ver;
    @Column(name="LAST_UPDATED_AT")
    private LocalDateTime lastUpdateAt;

}
