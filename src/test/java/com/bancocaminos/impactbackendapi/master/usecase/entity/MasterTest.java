package com.bancocaminos.impactbackendapi.master.usecase.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class MasterTest {

    @Test
    public void masterEntity() {
        Master master = new Master();
        master.setEnable2FA(true);
        master.setMaintenance(true);

        assertEquals(true, master.isEnable2FA());
        assertEquals(true, master.isMaintenance());
    }
}
