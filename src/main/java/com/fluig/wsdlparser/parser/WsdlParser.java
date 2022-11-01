package com.fluig.wsdlparser.parser;

import com.fluig.wsdlparser.service.vo.SoapResourceSchemaVO;
import com.ibm.wsdl.OperationImpl;
import com.ibm.wsdl.PortTypeImpl;
import com.ibm.wsdl.xml.WSDLReaderImpl;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLReader;
import java.util.List;

public class WsdlParser {

    private final WSDLReader reader;

    public WsdlParser() {
        this.reader = new WSDLReaderImpl();
    }

    public List<SoapResourceSchemaVO> parseWsdl(String endpoint) {
        try {
            Definition def = reader.readWSDL(endpoint);
            def.getAllPortTypes().forEach((key, value) -> {
                PortTypeImpl portType = (PortTypeImpl) value;
                portType.getOperations().forEach((oper) -> {
                    OperationImpl operation = (OperationImpl) oper;
                    System.out.println(operation.getName());
                });

                System.out.println("key: " + key + ", value: " + value);
            });
        } catch (WSDLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
