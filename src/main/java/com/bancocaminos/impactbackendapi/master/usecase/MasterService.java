package com.bancocaminos.impactbackendapi.master.usecase;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.master.usecase.entity.Master;
import com.bancocaminos.impactbackendapi.master.usecase.repository.MasterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MasterService implements IMasterService {

    @Autowired
    private MasterRepository masterRepository;

    @Override
    public Optional<Master> fetchMasterItem() {
        return masterRepository.findById(1L);
    }

    @Override
    public Boolean is2FAActive() {
        Optional<Master> master = fetchMasterItem();
        if (master.isPresent() && master.get().isEnable2FA()) {// comprobacion de 2FA
            return true;
        }
        return false;
    }
}
