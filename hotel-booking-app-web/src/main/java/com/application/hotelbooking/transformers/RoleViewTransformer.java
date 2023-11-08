package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.RoleView;
import com.application.hotelbooking.domain.RoleModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RoleViewTransformer {

    @Autowired
    private ModelMapper modelMapper;

    public RoleView transformToRoleView(RoleModel roleModel){
        return modelMapper.map(roleModel, RoleView.class);
    }

    public Collection<RoleModel> transformToRoleDTOs(Collection<RoleView> roleViews){
        return roleViews.stream()
                .map(roleView -> modelMapper.map(roleView, RoleModel.class))
                .collect(Collectors.toList());
    }
}
