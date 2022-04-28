package com.best.hello.controller.Deserialize;

import com.best.hello.controller.XXE.Student;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.serialization.ValidatingObjectInputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * 反序列化漏洞：序列化是将 Java 对象转换成字节流的过程。而反序列化是将字节流转换成 Java 对象的过程
 * java序列化的数据一般会以标记(ac ed 00 05)开头，base64编码后的特征为rO0AB
 * JAVA 常见的序列化和反序列化的方法有JAVA 原生序列化和 JSON 类（fastjson、jackson）序列化
 *
 * 审计的函数
 *      1. ObjectInputStream.readObject
 *      2. ObjectInputStream.readUnshared
 *      3. XMLDecoder.readObject
 *      4. Yaml.load
 *      5. XStream.fromXML
 *      6. ObjectMapper.readValue
 *      7. JSON.parseObject
 */

@Slf4j
@RestController
@RequestMapping("/Deserialize/readObject")
public class ObjectInputStream {

    @ApiOperation(value = "实现序列化与反序列化demo", notes = "将String对象obj序列化后写入文件object文件中，然后又从该文件反序列化得到该对象")
    @GetMapping("/test")
    public String test() {
        String obj = "hello world!";
        try {
            // 创建一个包含对象进行反序列化信息的”object”数据文件
            FileOutputStream fos = new FileOutputStream("object");
            ObjectOutputStream os = new ObjectOutputStream(fos);

            // writeObject()方法将obj对象写入object文件
            os.writeObject(obj);
            os.close();

            // 从文件中反序列化obj对象
            FileInputStream fis = new FileInputStream("object");
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(fis);

            // 恢复对象
            String obj2 = (String) ois.readObject();
            ois.close();
            return obj2;

        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * @vul readObject，读取输入流,并转换对象。ObjectInputStream.readObject() 方法的作用正是从一个源输入流中读取字节序列，再把它们反序列化为一个对象。如果此时ObjectInputStream对象的初始化参数来自外部请求输入参数则基本可以确定存在反序列化漏洞
     * @poc http://127.0.0.1:8888/Deserialize/readObject?base64=payload
     * payload：java -jar ysoserial-0.0.6-SNAPSHOT-BETA-all.jar CommonsCollections5 "open -a Calculator" | base64
     */
    @ApiOperation(value = "vul：readObject反序列化")
    @RequestMapping("/vul")
    public String readObject(String base64) {
        try {
            log.info("[vul] 执行反序列化：" + base64);
            BASE64Decoder decoder = new BASE64Decoder();

            // 坑：读取的base64内容，加号会被转空格
            base64 = base64.replace(" ", "+");

            byte[] bytes = decoder.decodeBuffer(base64);

            // 将字节转为输入流
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

            // 反序列化流，将序列化的原始数据恢复为对象
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(stream);
            in.readObject();
            in.close();
            log.info("[vul] 反序列化执行");
            return "反序列化漏洞";
        } catch (Exception e) {
            return "Payload：java -jar ysoserial-0.0.6-SNAPSHOT-BETA-all.jar CommonsCollections5 \"open -a Calculator\" | base64";
        }
    }


    @ApiOperation(value = "safe：反序列化类白/黑名单控制", notes = "Apache Commons IO的ValidatingObjectInputStream来校验反序列化的类")
    @RequestMapping("/safe")
    public String safe(String base64) {
        try {
            log.info("[safe] 执行反序列化");
            BASE64Decoder decoder = new BASE64Decoder();

            // 坑：读取的base64内容，加号会被转空格
            base64 = base64.replace(" ", "+");

            byte[] bytes = decoder.decodeBuffer(base64);

            // 将字节转为输入流
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

            // 使用 ValidatingObjectInputStream，只允许反序列化Student class
            ValidatingObjectInputStream ois = new ValidatingObjectInputStream(stream);
            ois.accept(Student.class);
            ois.readObject();

            return "ValidatingObjectInputStream";
        } catch (Exception e) {
            return e.toString();
        }
    }

}
