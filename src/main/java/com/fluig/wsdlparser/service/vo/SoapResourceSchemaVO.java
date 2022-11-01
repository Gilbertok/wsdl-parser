package com.fluig.wsdlparser.service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SoapResourceSchemaVO  implements Serializable  {

    private static final long serialVersionUID = 1L;

    private String portTypeName;
    private String operationName;
    private String bindingName;
    private String input;
    private String output;
    private String request;
    private String response;
    private Boolean deprecated;

    public SoapResourceSchemaVO(String portTypeName, String operationName, String bindingName) {
        this.portTypeName = portTypeName;
        this.operationName = operationName;
        this.bindingName = bindingName;
        this.deprecated = false;
    }

}
