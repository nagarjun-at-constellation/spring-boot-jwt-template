package com.bancocaminos.impactbackendapi.user.usecase.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.bancocaminos.impactbackendapi.core.constants.GenericApiConstants;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Validated
@Table(name = "users")
@NoArgsConstructor
public class Users {

    private static final String BOOLEAN_DEFAULT_TRUE = "boolean default true";
    private static final String MOBILE_NUMBER_CONSTANT = "mobile_number";
    private static final String PREFIX_COLUMN = "prefix";
    private static final String SECRET_2FA_KEY = "secret_2fa_key";
    private static final String USING_2FA = "using_2fa";
    private static final String USERNAME_COLUMN = "username";
    private static final String PASSWORD_COLUMN = "password";
    private static final String ROLE_COLUMN = "role";
    public static final String UPDATED_AT_COLUMN = "updated_at";
    public static final String CREATED_AT_COLUMN = "created_at";
    public static final String ACTIVE_COLUMN = "active";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GenericApiConstants.ID_SEQ)
    @SequenceGenerator(name = GenericApiConstants.ID_SEQ, sequenceName = GenericApiConstants.ID_SEQ, allocationSize = 100)
    private Long id;

    @Column(name = USERNAME_COLUMN, nullable = false)
    private String username;

    @Column(name = PASSWORD_COLUMN, nullable = false)
    private String password;

    @Column(name = ROLE_COLUMN, nullable = false)
    private String role;

    @Column(name = ACTIVE_COLUMN, columnDefinition = BOOLEAN_DEFAULT_TRUE)
    private boolean active;

    @Column(name = USING_2FA, columnDefinition = BOOLEAN_DEFAULT_TRUE)
    private boolean using2FA;

    @Column(name = SECRET_2FA_KEY)
    @Size(max = 6)
    private String secret2FAKey;

    @Column(name = PREFIX_COLUMN)
    @NotBlank
    private String prefix;

    @Column(name = MOBILE_NUMBER_CONSTANT)
    @Size(min = 9, max = 9)
    private String mobileNumber;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = CREATED_AT_COLUMN, nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = UPDATED_AT_COLUMN, nullable = false)
    @LastModifiedDate
    private Date updatedAt;
}