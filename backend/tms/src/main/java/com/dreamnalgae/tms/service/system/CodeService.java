package com.dreamnalgae.tms.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dreamnalgae.tms.model.system.CodeDTO;
import com.dreamnalgae.tms.repository.system.CodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeService {
    private final CodeRepository repo;

    public List<CodeDTO> getCodesByGroup(String groupCd) {
        return repo.findByGroupCd(groupCd);
    }
    
}
