package com.blossomproject.core.common.service;

import com.blossomproject.core.common.dto.AbstractDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReadOnlyService<DTO extends AbstractDTO> extends ReadOnlyServicePlugin {

  Page<DTO> getAll(Pageable pageable);

  List<DTO> getAll(List<Long> ids);

  List<DTO> getAll();

  DTO getOne(Long id);

}
