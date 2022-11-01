package com.fluig.wsdlparser.service.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ImportWsdlVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String wsdlEndPoint;

    private String applicationId;

    private String serviceId;

    private Boolean update;
}
