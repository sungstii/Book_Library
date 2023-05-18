package server.book_library.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;
import server.book_library.security.auths.filter.JwtAuthenticationFilter;
import server.book_library.security.auths.filter.JwtVerificationFilter;
import server.book_library.security.auths.handler.MemberAuthenticationFailureHandler;
import server.book_library.security.auths.handler.MemberAuthenticationSuccessHandler;
import server.book_library.security.auths.jwt.JwtTokenizer;
import server.book_library.security.auths.utils.CustomAuthorityUtils;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final CorsFilter corsFilter;

    public SecurityConfiguration(JwtTokenizer jwtTokenizer, CustomAuthorityUtils customAuthorityUtils, CorsFilter corsFilter) {
        this.jwtTokenizer = jwtTokenizer;
        this.customAuthorityUtils = customAuthorityUtils;
        this.corsFilter = corsFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeRequests(authorize -> authorize
                        .antMatchers(HttpMethod.GET, "/books/**").permitAll()
                        .antMatchers( "/books/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/libraries/**").permitAll()
                        .antMatchers( "/libraries/**").hasRole("ADMIN")
                        .antMatchers("/libraries/inventories/**").hasRole("ADMIN")
                        .antMatchers("/loan/**").hasRole("USER")
                        .antMatchers("/return/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE,"/members/**").hasRole("USER")
                        .antMatchers( "/members/**").permitAll()
                        .anyRequest().permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, customAuthorityUtils);

            builder
                    .addFilter(corsFilter)
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtVerificationFilter.class);
        }
    }
}
