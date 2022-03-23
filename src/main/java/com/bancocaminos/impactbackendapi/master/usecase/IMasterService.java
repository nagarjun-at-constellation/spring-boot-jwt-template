package com.bancocaminos.impactbackendapi.master.usecase;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.master.usecase.entity.Master;

public interface IMasterService {
    public Optional<Master> fetchMasterItem();

    public Boolean is2FAActive();
}
