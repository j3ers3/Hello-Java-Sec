package com.best.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * 反序列化漏洞
 * 代码审计：java序列化的数据一般会以标记(ac ed 00 05)开头，base64编码后的特征为rO0AB
 * 反序列化函数调用点：
 * ObjectInputStream.readObject
 * ObjectInputStream.readUnshared
 * XMLDecoder.readObject
 * Yaml.load
 * XStream.fromXML
 * ObjectMapper.readValue
 * JSON.parseObject
 */
@RestController
@RequestMapping("/Deserialize")
public class Deserialize {

    /**
     * 实现序列化与反序列化demo
     * 将 String 对象 obj 序列化后写入文件 object 文件中，后又从该文件反序列化得到该对象
     * 查看文件：xxd object
     */
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
            ObjectInputStream ois = new ObjectInputStream(fis);

            // 恢复对象
            String obj2 = (String) ois.readObject();
            ois.close();
            return obj2;

        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * @vul readObject，读取输入流,并转换对象。readObject() 方法的作用正是从一个源输入流中读取字节序列，再把它们反序列化为一个对象。
     * @poc http://127.0.0.1:8888/Deserialize/readObject?base64=payload
     * payload：java -jar ysoserial-0.0.6-SNAPSHOT-BETA-all.jar CommonsCollections5 "open -a Calculator" | base64
     */
    @RequestMapping("/readObject")
    public String readObject(String base64) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();

            // 坑：读取的base64内容，加号会被转空格
            base64 = base64.replace(" ", "+");

            byte[] bytes = decoder.decodeBuffer(base64);

            // 将字节转为输入流
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);

            // 反序列化流，将序列化的原始数据恢复为对象
            ObjectInputStream in = new ObjectInputStream(stream);
            in.readObject();
            in.close();
            return "反序列化漏洞";
        } catch (Exception e) {
            return e.toString();
        }
    }
}
