# ATP-server

# Start new project #
- Go to [Spring initializer](https://start.spring.io/)
- Prepare you project metadata:
 * Type: Maven project
 * Packaging: JAR
 * Java Version: 1.8
 * Spring boot version: 1.2.3
- Select project dependencies:
 * Web: Web
 * Data: JPA
 * Database: H2
- Genereate and download you project starter
- After unpack you can build you project with command __mvn install__
- When all required dependencies are downloaded and your project builds with success you can start it with command __mvn spring-boot:run__. Run it to check everything works as expected

# Integrate MorseCodeTranslator #
- Add to pom.xml dependency of [MorseCodeTranslator](https://github.com/konczak/MorseCodeTranslator) which you should have already download and build localy because its not available through Maven repo
```xml
<dependency>
   <groupId>pl.konczak</groupId>
   <artifactId>MorseCodeTranslator</artifactId>
   <version>1.0</version>
</dependency>
```
- Create package _config_ where you should put additional configuration for you Spring boot application
- Inform Spring it should search for your custom beans inside you package. Edit your Application.java file and add annotation _@ComponentScan(basePackages = "YOUR-MAIN-PACKAGE-PATH")_
- Add inside it new class called BeansConfig and mark it with _@Configuration_ annotation
- In BeansConfig register MorseCodeTranslator as bean using code:
```java
@Bean
public IMorseCodeTranslator morseCodeTranslator() {
    return new MorseCodeTranslator();
}
```

# REST API #
- Create packages _rest_ and _rest.model_
- Create new models _rest.model.EnglishText_ and _rest.model.MorseCode_. Both has only one private field _String value_ with getters and setters
- Create REST API class MorseCodeAPI with annotation _@RestController_ to inform Spring that class will serve some API. Also add _@RequestMapping_ annotation with value _"/api/morsecode"_ what means that main context of this API is /api/morsecode
```java
@RestController
@RequestMapping("/api/morsecode")
```
- Inject IMorseCodeTranslator into our API class because we will use it next steps
```java
@Autowired
private IMorseCodeTranslator morseCodeTranslator;
```
- Register POST /api/morsecode/encode endpoint which as body of request takes EnglishText and returns MorseCode
```java
@RequestMapping(value = "/encode",
                method = RequestMethod.POST)
    public HttpEntity<MorseCode> encode(@RequestBody EnglishText englishText) {
    String value = morseCodeTranslator.encode(englishText.getValue());
    MorseCode morseCode = new MorseCode();
    morseCode.setValue(value);

    return new ResponseEntity<>(morseCode, HttpStatus.OK);
}
```
- Register POST /api/morsecode/decode endpoint which as body of request takes MorseCode and returns EnglishText
```java
@RequestMapping(value = "/decode",
                method = RequestMethod.POST)
public HttpEntity<EnglishText> decode(@RequestBody MorseCode morseCode) {
    String value = morseCodeTranslator.decode(morseCode.getValue());
    EnglishText englishText = new EnglishText();
    englishText.setValue(value);

    return new ResponseEntity<>(englishText, HttpStatus.OK);
}
```
- Final result should be at least similar to that:
```java
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
- Add Swagger to automate documentation of your REST API
```xml
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
- Add additional Maven repository where swagger-spring-mvc-ui is stored
```xml
    <repositories>
        <repository>
            <id>oss-jfrog-artifactory</id>
            <name>oss-jfrog-artifactory-releases</name>
            <url>http://oss.jfrog.org/artifactory/oss-release-local</url>
        </repository>
    </repositories>
```
- Annotate you application class with _@EnableSwagger_ to publish documentation of your API
- Run your application using __mvn spring-boot:run__ when it is ready open [documentation](http://localhost:8080/sdoc.jsp) - try to use your endpoints.
__Note:__ You have to remove ${pageContext.request.contextPath} from generated Swagger documentation.

# Save your data in database #
- Create package _db_, _db.entity_, _db.repo_
- Create new entity _rest.model.Translation_ which implements _Serializable_ and has private fields _long id_, _String englishText_, _String morseCode_, _LocalDateTime createdAt_ and getters
- To make Translation class proper entity annotate class with _@Entity_ and field _long id_ with _@Id_ and _@GeneratedValue(strategy = GenerationType.AUTO)_
- Add protected constructor without arguments so Hibernate could use it
- Add public constructor with arguments EnglishText and MorseCode and assign their values to proper fields. Also initialize field createdAt with actual date
```java
@Entity
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String englishText;

    private String morseCode;

    private LocalDateTime createdAt;

    protected Translation() {
    }

    public Translation(EnglishText englishText, MorseCode morseCode) {
        this.englishText = englishText.getValue();
        this.morseCode = morseCode.getValue();
        this.createdAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getEnglishText() {
        return englishText;
    }

    public String getMorseCode() {
        return morseCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
```
- Create new repository _db.repo.ITranslationRepository_ which extends _JpaRepository<Translation, Long>_
- Add in MorseCodeAPI new filed which will be injected by Spring _ITranslationRepository translationRepository_
```java
@Autowired
private ITranslationRepository translationRepository;
```
- Modify MorseCodeAPI endpoint encode and decode to save new translations using _ITranslationRepository_
```java
Translation translation = new Translation(englishText, morseCode);
translationRepository.save(translation);
```

# Expose your entity through REST API #
- Create REST API class TranslationAPI with annotation _@RestController_ to inform Spring that class will serve some API. Also add _@RequestMapping_ annotation with value _"/api/translation"_ what means that main context of this API is /api/translation
```java
@RestController
@RequestMapping("/api/translation")
```
- Inject ITranslationRepository into our API class because we will use it next steps
```java
@Autowired
private ITranslationRepository translationRepository;
```
- Register GET /api/translation endpoint which returns List of all Translation
```java
@RequestMapping(method = RequestMethod.GET)
    public List<Translation> list() {
    return translationRepository.findAll();
}
```
- Register GET /api/translation/{transationId} endpoint which takes ID and returns proper Translation
```java
@RequestMapping(value = "/api/translation/{translationId}",
                method = RequestMethod.GET)
public HttpEntity<Translation> get(@PathVariable long translationId) {
    Translation translation = translationRepository.findOne(translationId);

    if (translation == null) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    } else {
        return new ResponseEntity<>(translation, HttpStatus.OK);
    }
}
```
- Final result should be at least similar to that:
```java
@RestController
@RequestMapping("/api/translation")
public class TranslationAPI {

    @Autowired
    private ITranslationRepository translationRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Translation> list() {
        return translationRepository.findAll();
    }

    @RequestMapping(value = "/api/translation/{translationId}",
                    method = RequestMethod.GET)
    public HttpEntity<Translation> get(@PathVariable long translationId) {
        Translation translation = translationRepository.findOne(translationId);

        if (translation == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(translation, HttpStatus.OK);
        }
    }
}
```
- Run your application using __mvn spring-boot:run__ when it is ready open [documentation](http://localhost:8080/sdoc.jsp) use encode or decode endpoint and next serch for all Translations.
__Note:__ You have to remove ${pageContext.request.contextPath} from generated Swagger documentation.
