# ATP-server

# Start new project #
1. Go to [Spring initializer](https://start.spring.io/)
2. Prepare you project metadata:
 * Type: Maven project
 * Packaging: JAR
 * Java Version: 1.8
 * Spring boot version: 1.2.3
3. Select project dependencies:
 * Web: Web
 * Data: JPA
 * Database: H2
4. Genereate and download you project starter
5. After unpack you can build you project with command __mvn install__
6. When all required dependencies are downloaded and your project builds with success you can start it with command __mvn spring-boot:run__. Run it to check everything works as expected

# Integrate MorseCodeTranslator #
1. Add to pom.xml dependency of [MorseCodeTranslator](https://github.com/konczak/MorseCodeTranslator) which you should have already download and build localy because its not available through Maven repo
```
<dependency>
   <groupId>pl.konczak</groupId>
   <artifactId>MorseCodeTranslator</artifactId>
   <version>1.0</version>
</dependency>
```
2. Create package _config_ where you should put additional configuration for you Spring boot application
3. Inform Spring it should search for your custom beans inside you package. Edit your Application.java file and add annotation _@ComponentScan(basePackages = "YOUR-MAIN-PACKAGE-PATH")_
4. Add inside it new class called BeansConfig and mark it with _@Configuration_ annotation
5. In BeansConfig register MorseCodeTranslator as bean using code:
```
@Bean
public IMorseCodeTranslator morseCodeTranslator() {
    return new MorseCodeTranslator();
}
```

# REST API #
1. Create packages rest and rest.model
2. Create new models EnglishText and MorseCode. Both has only one private field _String value_ with getters and setters
3. Create REST API class MorseCodeAPI with annotation @RestController to inform Spring that class will serve some API. Also add @RequestMapping annotation with value "/api/morsecode" what means that main context of this API is /api/morsecode
```
@RestController
@RequestMapping("/api/morsecode")
```
4. Inject IMorseCodeTranslator into our API class because we will use it next steps
```
@Autowired
private IMorseCodeTranslator morseCodeTranslator;
```
5. Register POST /api/morsecode/encode endpoint which as body of request takes EnglishText and returns MorseCode
```
@RequestMapping(value = "/encode",
                method = RequestMethod.POST)
    public HttpEntity<MorseCode> encode(@RequestBody EnglishText englishText) {
    String value = morseCodeTranslator.encode(englishText.getValue());
    MorseCode morseCode = new MorseCode();
    morseCode.setValue(value);

    return new ResponseEntity<>(morseCode, HttpStatus.OK);
}
```
6. Register POST /api/morsecode/decode endpoint which as body of request takes MorseCode and returns EnglishText
```
@RequestMapping(value = "/decode",
                method = RequestMethod.POST)
public HttpEntity<EnglishText> decode(@RequestBody MorseCode morseCode) {
    String value = morseCodeTranslator.decode(morseCode.getValue());
    EnglishText englishText = new EnglishText();
    englishText.setValue(value);

    return new ResponseEntity<>(englishText, HttpStatus.OK);
}
```
7. Final result should be at least similar to that:
```
@RestController
@RequestMapping("/api/morsecode")
public class MorseCodeAPI {

    @Autowired
    private IMorseCodeTranslator morseCodeTranslator;

    @RequestMapping(value = "/encode",
                    method = RequestMethod.POST)
    public HttpEntity<MorseCode> encode(@RequestBody EnglishText englishText) {
        String value = morseCodeTranslator.encode(englishText.getValue());
        MorseCode morseCode = new MorseCode();
        morseCode.setValue(value);

        return new ResponseEntity<>(morseCode, HttpStatus.OK);
    }

    @RequestMapping(value = "/decode",
                    method = RequestMethod.POST)
    public HttpEntity<EnglishText> decode(@RequestBody MorseCode morseCode) {
        String value = morseCodeTranslator.decode(morseCode.getValue());
        EnglishText englishText = new EnglishText();
        englishText.setValue(value);

        return new ResponseEntity<>(englishText, HttpStatus.OK);
    }

}
```
8. Add Swagger to automate documentation of your REST API
```
<dependency>
    <groupId>com.mangofactory</groupId>
    <artifactId>swagger-springmvc</artifactId>
    <version>1.0.2</version>
</dependency>
<dependency>
    <groupId>org.ajar</groupId>
    <artifactId>swagger-spring-mvc-ui</artifactId>
    <version>0.4</version>
</dependency>
```
9. Annotate you application class with @EnableSwagger to publish documentation of your API
10. Run your application using __mvn spring-boot:run__ when it is ready open [documentation](http://localhost:8080/sdoc.jsp) - try to use your endpoints. Note: You have to remove ${pageContext.request.contextPath} from generated Swagger documentation.