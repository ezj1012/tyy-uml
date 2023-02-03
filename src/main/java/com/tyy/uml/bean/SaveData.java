package com.tyy.uml.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SaveData {

    private UMLConfig config;

    private List<UMLModel> models;

    public SaveData() {
        this.config = new UMLConfig();
        this.models = new ArrayList<>();
    }

    public SaveData(UMLConfig config, List<UMLModel> models) {
        super();
        this.config = config;
        this.models = models;
    }

}
