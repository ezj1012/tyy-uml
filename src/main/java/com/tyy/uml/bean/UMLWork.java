package com.tyy.uml.bean;

import java.util.ArrayList;
import java.util.List;

import com.tyy.uml.context.UMLGUIConfig;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UMLWork {

    private UMLGUIConfig config;

    private List<UMLProject> projects;

    private boolean autoOpen;

    private String baseDir;

    public UMLWork() {
        config = new UMLGUIConfig();
        projects = new ArrayList<UMLProject>();
    }

}
