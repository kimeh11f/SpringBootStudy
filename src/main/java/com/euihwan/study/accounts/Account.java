package com.euihwan.study.accounts;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private String email;

    private String fullName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joined;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
}
