# ☕️ Hello Java Sec ![Stage](https://img.shields.io/badge/Release-DEV-brightgreen.svg) ![Build Status](https://img.shields.io/badge/Version-1.10-red.svg)
> Java漏洞平台，结合漏洞代码和安全编码，帮助研发同学理解和减少漏洞，代码仅供参考

![](media/16304933749187.jpg)


- 默认账号：admin/admin

## Vulnerability
- [x] SQLi
- [x] XSS
- [x] RCE
- [x] Deserialize
- [x] SSTI
- [x] SpEL
- [x] SSRF
- [x] Directory Traversal
- [x] Redirect
- [x] CSRF
- [x] File Upload
- [x] XXE
- [x] Actuator
- [x] Fastjson
- [x] Xstream
- [x] Log4shell
- [x] JNDI
- [x] Dos
- [x] Xpath
- [x] Jwt
- [ ] more and more

![](media/16304936834843.jpg)

## Run
### IDEA
配置数据库连接，数据库文件`src/main/resources/db.sql`
```
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/test
spring.datasource.username=root
spring.datasource.password=1234567
```

### Jar运行
> JDK 1.8环境
```
git clone https://github.com/j3ers3/Hello-Java-Sec
cd Hello-Java-Sec
mvn clean package -DskipTests
java -jar target/hello-1.0.0-SNAPSHOT.jar
```

### Docker运行
```
mvn clean package
./deploy.sh
```
![](media/16512152886514.jpg)


## 技术架构
- Java 1.8
- SpringBoot 4.0
- Bootstrap 4.6.0
- Codemirror 5.62.0
