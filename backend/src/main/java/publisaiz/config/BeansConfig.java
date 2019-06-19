package publisaiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import publisaiz.datasources.database.entities.User;
import publisaiz.tools.xls.XlsHandleProcessing;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableSwagger2
//@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableScheduling
@EnableWebMvc
public class BeansConfig {


    public BeansConfig( ) {
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "PUBLISAIZ API",
                "API.",
                "API 1",
                "ENJOY",
                new Contact("Michał Brzeziński", "www.michalbrzezinski.org", "michal@michalbrzezinski.org"),
                "License of API", "API license URL", Collections.emptyList());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public XlsHandleProcessing handleProcessing() {
        return new XlsHandleProcessing();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public DateTimeProvider getDate() {
        return () -> Optional.ofNullable(ZonedDateTime.now());
    }

    @Bean
    Converter<ZonedDateTime, LocalDateTime> convZonedDateTime2LocalDateTime() {
        return (z) -> z.toLocalDateTime();
    }

    @Bean
    Converter<LocalDateTime, ZonedDateTime> convLocalDateTime2ZonedDateTime() {
        return (l) -> l.atZone(ZoneId.systemDefault());
    }

    @Bean
    Converter<String, User> convString2User() {
        return s -> new User(s);
    }
}
