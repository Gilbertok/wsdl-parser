package com.fluig.wsdlparser.parser;

import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlUtils;
import com.eviware.soapui.support.xml.XmlUtils;
import com.fluig.wsdlparser.service.vo.SoapResourceSchemaVO;
import org.apache.xmlbeans.XmlObject;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SoapUiWsdlParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapUiWsdlParser.class);
    private final List<WsdlInterface> wsdlInterfaces;

    public SoapUiWsdlParser(String endpoint) {
        this.wsdlInterfaces = this.getWSDL(endpoint);
    }

    private List<WsdlInterface> getWSDL(String endpoint) {
        try {
            WsdlProject project = new WsdlProject();
            return Arrays.asList(WsdlInterfaceFactory.importWsdl(project, endpoint, true));
        } catch (Exception exp) {
            LOGGER.warn("Ocorreu um erro na leitura do WSDL");
            exp.printStackTrace();
        }
        return null;
    }

    public List<WsdlInterface> getWsdlInterfaces() {
        return wsdlInterfaces;
    }

    public WsdlProject getProject() {
        return wsdlInterfaces.stream().findFirst().get().getProject();
    }

    public List<SoapResourceSchemaVO> parseWsdl() {
        List<SoapResourceSchemaVO> resourceSchemes = new ArrayList<>();
        for (WsdlInterface wsdlInterface : this.getWsdlInterfaces()) {
            String portTypeName = wsdlInterface.getBinding().getPortType().getQName().getLocalPart();
            String bindingName = wsdlInterface.getBinding().getQName().getLocalPart();
            for (AbstractWsdlModelItem<?> message : wsdlInterface.getAllMessages()) {
                WsdlRequest request = (WsdlRequest) message;
                WsdlOperation wsdlOperation = request.getOperation();
                String requestString = wsdlOperation.createRequest(true);
                String responseString = wsdlOperation.createResponse(true);
                try {
                    String input = this.getSchemaXML(wsdlOperation, requestString, false);
                    String output = this.getSchemaXML(wsdlOperation, responseString, true);
                    SoapResourceSchemaVO resourceSchema = new SoapResourceSchemaVO(portTypeName, wsdlOperation.getName(), bindingName);
                    resourceSchema.setInput(input);
                    resourceSchema.setOutput(output);
                    resourceSchema.setRequest(requestString);
                    resourceSchema.setResponse(responseString);
//                    resourceSchema.setInputParts(Arrays.asList(request.getRequestParts()));
//                    resourceSchema.setOutputParts(Arrays.asList(request.getResponseParts()));
                    resourceSchemes.add(resourceSchema);
                } catch (Exception ex) {
                    LOGGER.warn(String.format("Ocorreu um erro no parse do WSDL: %1s", ex.getMessage()));
                    ex.printStackTrace();
                }
            }
        }
        return resourceSchemes;
    }

    private String getSchemaXML(WsdlOperation wsdlOperation, String xmlString, boolean response) throws Exception {
        String namespaceEnv = wsdlOperation.getInterface().getWsdlContext().getSoapVersion().getEnvelopeNamespace();
        String namespaceNs = WsdlUtils.getTargetNamespace(wsdlOperation.getInterface().getWsdlContext().getDefinition());
        XmlObject msgXml = XmlUtils.createXmlObject(xmlString);

        String select = "declare namespace env='" + namespaceEnv + "';declare namespace ns='" + namespaceNs + "';" +
                "$this/env:Envelope/env:Body/ns:" + wsdlOperation.getBindingOperation().getName() + (response ? "Response" : "");
        XmlObject[] paths = msgXml.selectPath(select);
        JSONObject json = this.removeKeysXml(XML.toJSONObject(Arrays.toString(paths)));
        return XML.toString(json);
    }

    private JSONObject removeKeysXml(JSONObject json) {
        if (json.keySet().stream().anyMatch(key -> key.contains("xml-fragment"))) {
            json = json.getJSONObject("xml-fragment");
        }
        if (json.keySet().stream().anyMatch(key -> key.toLowerCase().contains("result"))) {
            String result = json.keySet().stream().filter(key -> key.toLowerCase().contains("result")).findFirst().orElse("");
            json = json.getJSONObject(result);
        }
        List<String> keysXml = json.keySet().stream().filter(key -> key.contains("xmlns")).collect(Collectors.toList());
        keysXml.forEach(json::remove);
        return json;
    }


}
