package com.application.hotelbooking.transformers;

import com.application.hotelbooking.domain.Role;
import com.application.hotelbooking.domain.RoleModel;
import com.application.hotelbooking.domain.RoleView;
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

    public RoleModel transformToRoleModel(RoleView roleView){
        return modelMapper.map(roleView, RoleModel.class);
    }

    public Collection<RoleModel> transformToRoleModels(Collection<RoleView> roleViews){
        return roleViews.stream()
                .map(roleView -> modelMapper.map(roleView, RoleModel.class))
                .collect(Collectors.toList());
    }

//    public Collection<RoleView> transformToRoleViews(Collection<RoleModel> roleModels){
//        return roleModels.stream()
//                .map(roleModel -> modelMapper.map(roleModel, RoleView.class))
//                .collect(Collectors.toList());
//    }
}
