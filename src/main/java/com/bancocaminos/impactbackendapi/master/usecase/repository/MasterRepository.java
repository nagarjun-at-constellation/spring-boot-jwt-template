package com.bancocaminos.impactbackendapi.master.usecase.repository;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.master.usecase.entity.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterRepository extends JpaRepository<Master, String> {
    Optional<Master> findById(Long id);
}