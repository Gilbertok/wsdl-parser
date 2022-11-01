package com.fluig.wsdlparser.controller;

import com.fluig.wsdlparser.service.SoapResourceService;
import com.fluig.wsdlparser.service.vo.ImportWsdlVO;
import com.fluig.wsdlparser.service.vo.SoapResourceSchemaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/soap-resources"})
public class SoapResourceController {

    private final SoapResourceService service;

    @Autowired
    public SoapResourceController(SoapResourceService service) {
        this.service = service;
    }

    @PostMapping(path = "/import-wsdl", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importWsdl(@Validated @RequestBody ImportWsdlVO vo)  {
        List<SoapResourceSchemaVO> resourceSchemes = service.importWsdl(vo);
        return ResponseEntity.status(HttpStatus.OK).body(resourceSchemes);
    }

    @PostMapping(path = "/import-wsdl2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> importWsdlNew(@Validated @RequestBody ImportWsdlVO vo)  {
        List<SoapResourceSchemaVO> resourceSchemes = service.importWsdlNew(vo);
        return ResponseEntity.status(HttpStatus.OK).body(resourceSchemes);
    }


}
