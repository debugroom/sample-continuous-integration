package org.debugroom.sample.continuous.integration.app.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResource implements Serializable {

    @NotNull
    private long id;
    @NotNull
    private String firstName;
    @NotNull
    private String familyName;

}
