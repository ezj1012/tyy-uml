package com.tyy.uml.core.ctx.model;

import java.util.ArrayList;
import java.util.List;

import com.tyy.uml.core.ctx.bean.UMLGUIConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UMLProjectData extends UMLProject {

    private UMLGUIConfig config;

    private List<UMLModel> models;

    public UMLProjectData() {
        this.config = new UMLGUIConfig();
        this.models = new ArrayList<>();
    }

    public UMLProjectData(UMLGUIConfig config, List<UMLModel> models) {
        super();
        this.config = config;
        this.models = models;
    }

}
