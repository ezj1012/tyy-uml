package com.tyy.uml.core.ctx.model;

import lombok.Data;

@Data
public class UMLProject {

    private String name;

    private String path;

    public UMLProject() {

    }

    public UMLProject(String name, String path) {
        super();
        this.name = name;
        this.path = path;
    }

}
