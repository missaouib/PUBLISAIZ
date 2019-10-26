package publisaiz.config.cors;

import org.springframework.web.cors.CorsConfiguration;

public class OriginsAllowedSetter {
    public OriginsAllowedSetter() {
    }

    public void setAllowed(CorsConfiguration config) {

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        config.addAllowedOrigin("http://publisaiz.fun");
        config.addAllowedOrigin("http://www.publisaiz.fun");
        config.addAllowedOrigin("https://publisaiz.fun");
        config.addAllowedOrigin("https://www.publisaiz.fun");

        config.addAllowedOrigin("http://publisaiz");
        config.addAllowedOrigin("http://www.publisaiz");

        config.addAllowedOrigin("http://localhost:4200");

        config.addAllowedOrigin("http://publisaiz.pl");
        config.addAllowedOrigin("http://www.publisaiz.pl");
        config.addAllowedOrigin("https://publisaiz.pl");
        config.addAllowedOrigin("https://www.publisaiz.pl");
    }
}