package com.bancocaminos.impactbackendapi.master.usecase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.bancocaminos.impactbackendapi.core.constants.GenericApiConstants;

import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Validated
@Table(name = "master")
@NoArgsConstructor
public class Master {

    private static final String ENABLE_2FA_CONSTANT = "enable_2fa";
    private static final String MAINTENANCE_CONSTANT = "maintenance";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GenericApiConstants.ID_SEQ)
    @SequenceGenerator(name = GenericApiConstants.ID_SEQ, sequenceName = GenericApiConstants.ID_SEQ, allocationSize = 100)
    private Long id;

    @Column(name = MAINTENANCE_CONSTANT, columnDefinition = "boolean default false")
    private boolean maintenance;

    @Column(name = ENABLE_2FA_CONSTANT, columnDefinition = "boolean default false")
    private boolean enable2FA;
}
