package com.bancocaminos.impactbackendapi.master.usecase.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.bancocaminos.impactbackendapi.master.usecase.MasterService;
import com.bancocaminos.impactbackendapi.master.usecase.entity.Master;
import com.bancocaminos.impactbackendapi.master.usecase.repository.MasterRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class MasterServiceTest {

    @Autowired
    private MasterService masterService;

    @MockBean
    private MasterRepository masterRepository;

    @TestConfiguration
    static class MasterServiceTestContextConfiguration {

        @Bean
        public MasterService masterService() {
            return new MasterService();
        }

    }

    @Test
    public void fetchMasterItemSuccessfully() {
        Master master = new Master();
        master.setId(1L);
        Mockito.when(getMasterRepository().findById(1L)).thenReturn(Optional.of(master));
        Optional<Master> masterResponse = masterService.fetchMasterItem();
        assertTrue(masterResponse.isPresent());
        verify(getMasterRepository(), times(1)).findById(1L);
    }

    public MasterService getMasterService() {
        return this.masterService;
    }

    public void setMasterService(MasterService masterService) {
        this.masterService = masterService;
    }

    public MasterRepository getMasterRepository() {
        return this.masterRepository;
    }

    public void setMasterRepository(MasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }
}
