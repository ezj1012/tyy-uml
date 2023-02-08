package com.tyy.uml.core.ctx.model;

import lombok.Data;

@Data
public class UMLModelField {

    private String name;

    private String notes;

    private String javaType;

    private Long len;

    private Long dlen;

    private Long position;

}
