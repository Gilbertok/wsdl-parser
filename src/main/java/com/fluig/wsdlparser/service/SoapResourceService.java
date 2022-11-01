package com.fluig.wsdlparser.service;

import com.fluig.wsdlparser.parser.SoapUiWsdlParser;
import com.fluig.wsdlparser.parser.WsdlParser;
import com.fluig.wsdlparser.service.vo.ImportWsdlVO;
import com.fluig.wsdlparser.service.vo.SoapResourceSchemaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoapResourceService {

    private final WsdlParser parser;

    @Autowired
    public SoapResourceService(WsdlParser parser) {
        this.parser = parser;
    }

    public List<SoapResourceSchemaVO> importWsdl(ImportWsdlVO vo) {
        SoapUiWsdlParser parser = new SoapUiWsdlParser(vo.getWsdlEndPoint());
        return parser.parseWsdl();
    }

    public List<SoapResourceSchemaVO> importWsdlNew(ImportWsdlVO vo) {
        return parser.parseWsdl(vo.getWsdlEndPoint());
    }
}
