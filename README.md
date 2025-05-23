# Tiny Spring Framework

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java Version](https://img.shields.io/badge/Java-11%2B-brightgreen)](https://adoptopenjdk.net/)

A lightweight implementation of the Spring Framework core features, demonstrating advanced Java development skills and deep understanding of Spring's internal workings. This project helps you understand the essential principles of the Spring Framework.




## 📖 Core Modules

- **aop**: Implements AOP functionality with AspectJ integration, providing method interception and proxy creation capabilities.
  
- **beans**: Core container that handles bean instantiation, dependency injection, and lifecycle management.
  
- **context**: Application context implementation that builds on the beans module, adding annotation support and additional enterprise features.
  
- **io**: Resource abstraction for handling configuration files and classpath resources.
  
- **util**: Common utilities for reflection, type conversion, and other framework-wide operations.

- **web**: Comprehensive web support including:
  - Spring MVC-style RESTful API implementation
  - JDBC operations and transaction management
  - Embedded server support
  - Auto-configuration capabilities similar to Spring Boot
  - Filter chain processing
  - URL pattern matching and routing


## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher

## 📁 Project Structure
Container moulde includes the implementation of dpendency injection and AOP features.
``` marks
container
├── src
│   └── main
│       └── java
│           └── com
│               └── pythongong
│                   ├── aop
│                   │   ├── aspectj
│                   │   ├── autoproxy
│                   │   ├── interceptor
│                   │   └── proxy
│                   ├── beans
│                   │   ├── aware
│                   │   ├── config
│                   │   ├── factory
│                   │   └── impl
│                   ├── context
│                   │   ├── annotation
│                   │   └── impl
│                   ├── io
│                   ├── enums
│                   ├── exception
│                   ├── stereotype
│                   └── util
└── pom.xml
``` 
Web moudle includes the implementation of RESTful API, JDBC operations, and transaction management.

``` marks
web
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── pythongong
│       │           ├── context
│       │           │   └── impl
│       │           ├── exception
│       │           ├── restful
│       │           ├── util
│       │           └── utils
│       └── resources
└── pom.xml
```
## 🌟 Key Features
- **Web Framework Support**
  - RESTful API development with @RestController and @RequestMapping
  - JDBC operations with JdbcTemplate
  - Declarative transaction management with @Transactional
  - Embedded Tomcat server integration
  - Auto-configuration for web components
  - Request filtering and interceptor chain
  - Path variable and request body support
  - Transaction isolation level support

- **Dependency Injection Container**
  - Support for constructor, field, and method injection
  - Annotation-based configuration (@Component, @Autowired, @Value)
  - Java-based configuration (@Configuration, @Bean)

- **Advanced Bean Management**
  - Singleton and Prototype scopes
  - Bean lifecycle management (initialization and destruction)
  - Circular dependency resolution
  - Property value resolution and injection

- **Aspect-Oriented Programming (AOP)**
  - Support for @Before, @After, @Around, and @AfterReturning advice
  - Static pointcut checking with execution expressions
  - Ordered advice execution using back-tracing algorithms
  - AspectJ annotation support

- **Property Configuration**
  - Support for both .properties and YAML files
  - Advanced type conversion for property values
  - Custom property value converters
  - Environment abstraction

- **Component Scanning**
  - Automatic bean detection and registration
  - Custom component filtering
  - Base package configuration

- **Annotation Support**
  - `@Component` for general components
  - `@Configuration` for Java-based configuration
  - `@Bean` for method-based bean definitions
  - `@Autowired` for dependency injection
  - `@Value` for property injection
  - `@PostConstruct` and `@PreDestroy` for lifecycle callbacks



## 🔧 Technical Highlights

- **Web Technologies**
  - JDBC template for database operations
  - Declarative transaction management by thread-local storage
  - Servlet request handling for restful API
  - Lightweight usage for a web application


- **Functional Programming**
  - Stream API for bean processing pipelines
  - Optional for null-safe operations
  - Lambda expressions for event handlers
  - Method references for bean initialization
  - Immutable collections throughout
  - Records for immutable data carriers

- **Design Patterns**
  - Factory Pattern for bean creation
  - Singleton Pattern for bean scoping
  - Template Method Pvattern for bean processing
  - Observer Pattern for event handling

- **Best Practices**
  - Prefer composition over inheritance
  - Comprehensive JavaDoc documentation
  - Null-safe operations
  - Thread-safe implementations

- **Clean Architecture**
  - Modular design with clear separation of concerns
  - Extensive use of interfaces for flexibility
  - Comprehensive exception handling

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher



## 💻 Usage Example

```java
@Configuration
@ComponentScan("com.example")
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        return new BasicDataSource();
    }
}

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Value("${app.feature.enabled}")
    private boolean featureEnabled;
    
    @PostConstruct
    public void init() {
        // Initialization logic
    }
}

// AOP Example
@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.example.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        // Before method execution
    }

    @Around("execution(* com.example.service.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // Around method execution
        return joinPoint.proceed();
    }
}

// Property Configuration Example
@Configuration
public class AppConfig {
    @Value("${db.connection.timeout:30}")
    private Integer timeout;  // Automatic type conversion

    @Value("${app.settings}")
    private Settings settings;  // YAML structure mapping
}
```

## 🧪 Testing

```bash
mvn test
```

## 📚 Documentation

Comprehensive JavaDoc documentation is available for all public APIs. Generate the documentation using:

```bash
mvn javadoc:javadoc
```


## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Cheng Gong**
- GitHub: [@pythongong](https://github.com/pythongong)

---

⭐ If you find this project interesting, please consider giving it a star!

// Web Module Example
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = ?",
            new Object[]{id},
            User.class
        );
    }

    @Transactional
    @PostMapping("/users")
    public void createUser(@RequestBody User user) {
        jdbcTemplate.update(
            "INSERT INTO users (name, email) VALUES (?, ?)",
            user.getName(), user.getEmail()
        );
    }
}
