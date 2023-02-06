package com.tyy.uml.bean;

import java.util.ArrayList;
import java.util.List;

import com.tyy.uml.context.UMLGUIConfig;

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
