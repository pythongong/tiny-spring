# Tiny Spring Framework

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java Version](https://img.shields.io/badge/Java-11%2B-brightgreen)](https://adoptopenjdk.net/)

A lightweight implementation of the Spring Framework core features, demonstrating advanced Java development skills and deep understanding of Spring's internal workings. This project help
you understand the essential principle of the Spring Framework.

## 🌟 Key Features

- **Dependency Injection Container**
  - Support for constructor, field, and method injection
  - Annotation-based configuration (@Component, @Autowired, @Value)
  - Java-based configuration (@Configuration, @Bean)

- **Advanced Bean Management**
  - Singleton and Prototype scopes
  - Bean lifecycle management (initialization and destruction)
  - Circular dependency resolution
  - Property value resolution and injection

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