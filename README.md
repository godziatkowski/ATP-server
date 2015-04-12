# ATP-server

0. Go to [Spring initializer](https://start.spring.io/)
0. Prepare you project metadata:
 * Type: Maven project
 * Packaging: JAR
 * Java Version: 1.8
 * Spring boot version: 1.2.3
0. Select project dependencies:
 * Web: Web
 * Data: JPA
 * Database: H2
0. Genereate and download you project starter
0. After unpack you can build you project with command __mvn install__
0. When all required dependencies are downloaded and your project builds with success you can start it with command __mvn spring-boot:run__
0. Add to pom.xml dependency of [MorseCodeTranslator](https://github.com/konczak/MorseCodeTranslator) which you should have already download and build localy because its not available through Maven repo
```
<dependency>
   <groupId>pl.konczak</groupId>
   <artifactId>MorseCodeTranslator</artifactId>
   <version>1.0</version>
</dependency>
```
0. Create package _config_ where you should put additional configuration for you Spring boot application
0. Inform Spring it should search for your custom beans inside you package. Edit your Application.java file and add annotation _@ComponentScan(basePackages = "YOUR-MAIN-PACKAGE-PATH")_
0. Add inside it new class called BeansConfig and mark it with _@Configuration_ annotation
0. In BeansConfig register MorseCodeTranslator as bean using code:
```
@Bean
public IMorseCodeTranslator morseCodeTranslator() {
    return new MorseCodeTranslator();
}
```
