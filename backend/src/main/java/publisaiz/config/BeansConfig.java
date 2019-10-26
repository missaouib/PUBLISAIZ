package publisaiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import publisaiz.config.cors.OriginsAllowedSetter;
import publisaiz.entities.User;
import publisaiz.utils.xls.DataExtractorFacade;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Configuration
@EnableAspectJAutoProxy
@EnableScheduling
class BeansConfig {

    private final OriginsAllowedSetter originsAllowedSetter = new OriginsAllowedSetter();

    public BeansConfig() {
    }

    @Bean
    @Profile("prod")
    public CorsFilter corsFilterProd() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Allow anyone and anything access. Probably ok for Swagger spec
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        List<String> allowedOrigins = Arrays.asList("http://publisaiz.fun:8800/", "http://publisaiz.fun");
        List<String> allowedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With", "x-requested-with", "XMLHttpRequest");
        List<String> allowedMethods = Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS");
        config.setAllowedHeaders(allowedHeaders);
        config.setAllowedMethods(allowedMethods);
        config.setAllowedOrigins(allowedOrigins);
        config.setExposedHeaders(allowedHeaders);
        source.registerCorsConfiguration("/v2/api-docs", config);
        source.registerCorsConfiguration("/api", config);
        source.registerCorsConfiguration("/**", config);
        originsAllowedSetter.setAllowed(config);
        return new CorsFilter(source);
    }

    @Bean
    @Profile("dev")
    public CorsFilter corsFilterDev() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        List<String> allowedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With", "x-requested-with", "XMLHttpRequest");
        List<String> allowedMethods = Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS");
        List<String> allowedOrigins = Arrays.asList("*");
        corsConfig.setAllowedHeaders(allowedHeaders);
        corsConfig.setAllowedMethods(allowedMethods);
        corsConfig.setAllowedOrigins(allowedOrigins);
        corsConfig.setExposedHeaders(allowedHeaders);
        corsConfig.setMaxAge(36000L);
        corsConfig.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfig);
        originsAllowedSetter.setAllowed(corsConfig);
        return new CorsFilter(source);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DataExtractorFacade handleProcessing() {
        return new DataExtractorFacade();
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
        return () -> Optional.of(ZonedDateTime.now());
    }

    @Bean
    Converter<ZonedDateTime, LocalDateTime> convZonedDateTime2LocalDateTime() {
        return new Converter<ZonedDateTime, LocalDateTime>() {
            @Override
            public LocalDateTime convert(ZonedDateTime zonedDateTime) {
                return zonedDateTime.toLocalDateTime();
            }
        };
    }

    @Bean
    Converter<LocalDateTime, ZonedDateTime> convLocalDateTime2ZonedDateTime() {
        return new Converter<LocalDateTime, ZonedDateTime>() {
            @Override
            public ZonedDateTime convert(LocalDateTime localDateTime) {
                return localDateTime.atZone(ZoneId.systemDefault());
            }
        };
    }

    @Bean
    Converter<String, User> convString2User() {
        return new Converter<String, User>() {
            @Override
            public User convert(String s) {
                return new User(s);
            }
        };
    }

}
