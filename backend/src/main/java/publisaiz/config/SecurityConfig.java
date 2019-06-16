package publisaiz.config;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@Transactional
@EnableJpaAuditing
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private static final String LOGOUT = "/api/users/logout";
    private static final String ARTICLES = "/api/articles/all";
    private static final String ARTICLE = "/api/articles/article/**";
    private static final String FILES_ONE = "/api/files/one/**";
    private static final String USERS_WHOAMI = "/api/users/whoami";
    
    public static final String[] PUBLIC_PLACES = {ARTICLES, ARTICLE, LOGOUT, FILES_ONE, USERS_WHOAMI};

    private final SpringControllers springControllers;
    private final AuthenticationProviderImpl authenticationProvider;

    public SecurityConfig(SpringControllers springControllers, AuthenticationProviderImpl authenticationProvider) {
        this.springControllers = springControllers;
        this.authenticationProvider = authenticationProvider;
    }

    @NotNull
    public static String stringifyController(String getClassLevelAnnotation, String getMethodLevelAnnotation, String httpMethod) {
        String stringified = getClassLevelAnnotation.concat((getMethodLevelAnnotation.length() > 0 ? getMethodLevelAnnotation : ""));
        stringified = stringified.concat("$" + httpMethod);
        logger.info("stringified controller: [{}]", stringified);
        return stringified;
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        logger.info("configure(HttpSecurity [{}])", httpSecurity);
        final List<String> publicPlacesAsList = Arrays.asList(PUBLIC_PLACES);
        List<String> nonPublicControllers = getNonPublicControllers(publicPlacesAsList);
        var expr = init(httpSecurity);
        setHttpSecurityForPublicPlaces(expr, PUBLIC_PLACES);
        setHttpSecurityForAuthorities(expr, nonPublicControllers);
        options(expr);
        finish(expr);
    }

    @NotNull
    private ArrayList<String> getNonPublicControllers(List<String> publicPlacesAsList) {
        logger.info("getNonPublicControllers(List<String>  [{}]))", publicPlacesAsList);
        var result = springControllers.getControllers().stream()
                .map(c -> stringifyController(c.getClassLevelAnnotation(), c.getMethodLevelAnnotation(), c.getHttpMethod()))
                .filter(c -> !publicPlacesAsList.contains(c)).distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        publicPlacesAsList.forEach(c -> logger.info("publicPlace [{}]", c));
        result.forEach(c -> logger.info("nonPublicController [{}]", c));
        return result;
    }

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
    init(HttpSecurity httpSecurity) throws Exception {
        logger.info("init(HttpSecurity  [{}]))", httpSecurity);
        return httpSecurity
                .headers().frameOptions().sameOrigin().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests();
    }

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
    setHttpSecurityForPublicPlaces(
            ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expr,
            String[] publicPlaces) {
        logger.info("setHttpSecurityForPublicPlaces");
        return expr.antMatchers(publicPlaces).permitAll();
    }

    private ExpressionUrlAuthorizationConfigurer<?>.ExpressionInterceptUrlRegistry
    options(ExpressionUrlAuthorizationConfigurer<?>.ExpressionInterceptUrlRegistry expr) {
        logger.info("options");
        return expr.antMatchers(HttpMethod.OPTIONS).permitAll();
    }

    private void finish(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expr) throws Exception {
        expr.anyRequest().authenticated()
                .and().httpBasic()
                .and().logout().invalidateHttpSession(true)
                .and().csrf()
                // it affects disabling all accept GET due lack of propagation token via possibly via nginx
                // TODO: find workaround for csrfTokenRepository and docker architecture
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .disable()
        ;
    }

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
    setHttpSecurityForAuthorities
            (final ExpressionUrlAuthorizationConfigurer<HttpSecurity>
                    .ExpressionInterceptUrlRegistry expr,
                                  List<String> nonPublicControllers) {
        nonPublicControllers.forEach(auth -> {
            HttpMethod httpMethod = getHttpMethodFromAuthority(auth);
            String local = auth.substring(0, auth.lastIndexOf("$"));
            logger.info("setHttpSecurityForAuthorities httpMethod: [{}] local: [{}]", httpMethod, local);
            if (httpMethod != null)
                expr.antMatchers(httpMethod, local).hasAuthority(auth);
            else
                logger.error("auth: [{}]", auth);
        });
        return expr;
    }

    private HttpMethod getHttpMethodFromAuthority(String auth) {
        int index = auth.lastIndexOf("$");
        HttpMethod r = null;
        if (index > 0) {
            String method = auth.substring(index + 1);
            logger.info("auth [{}], HttpMethod [{}]", auth, r);
            r = HttpMethod.valueOf(method);
        }
        logger.info("auth [{}], HttpMethod [{}]", auth, r);
        return r;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

}
