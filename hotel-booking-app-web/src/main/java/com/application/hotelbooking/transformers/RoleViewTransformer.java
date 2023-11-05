package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.RoleView;
import com.application.hotelbooking.dto.RoleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RoleViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoleView transformToRoleView(RoleDTO roleDTO){
        return modelMapper.map(roleDTO, RoleView.class);
    }

    public Collection<RoleDTO> transformToRoleDTOs(Collection<RoleView> roleViews){
        return roleViews.stream()
                .map(roleView -> modelMapper.map(roleView, RoleDTO.class))
                .collect(Collectors.toList());
    }
}
