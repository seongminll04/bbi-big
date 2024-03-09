package bbibig.bbibig.global.config;

import bbibig.bbibig.domain.user.repository.UserRepository;
import bbibig.bbibig.global.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import bbibig.bbibig.global.login.handler.LoginFailureHandler;
import bbibig.bbibig.global.login.handler.LoginSuccessHandler;
import bbibig.bbibig.global.login.service.LoginService;
import bbibig.bbibig.global.security.jwt.JwtAuthenticationFilter;
import bbibig.bbibig.global.oauth2.handler.OAuth2LoginFailureHandler;
import bbibig.bbibig.global.oauth2.handler.OAuth2LoginSuccessHandler;
import bbibig.bbibig.global.oauth2.service.CustomOAuth2UserService;
import bbibig.bbibig.global.security.jwt.JwtService;
import bbibig.bbibig.global.security.redis.RedisRefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final JwtService jwtService;

    private final RedisRefreshTokenService redisRefreshTokenService;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    private static final String[] WHITE_LIST = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/ws/**",

            "/",
            "/favicon.ico",
            "/oauth2/**",

            "/chat/**"


    };

    @Bean
    protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // csrf 보안 사용 X
                .formLogin(AbstractHttpConfigurer::disable) // FormLogin 사용 X
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 사용 X
                .cors(c -> c.configurationSource(corsConfigurationSource())) // cors 허용 설정
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 X -> 토큰 사용
                .headers(h->h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                // url 별 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated()) // 그 외 경로는 모두 인증된 사용자만 접근 가능

                // 소셜 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig
                                .baseUri("/oauth2/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)));

        // 스프링 시큐리티 필터 순서 :
        // LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        // 아래 둘 순서 바꾸면 에러
        httpSecurity.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*
     * AuthenticationManager 설정 후 등록
     * */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(loginService);

        return new ProviderManager(daoAuthenticationProvider);
    }

    /*
     * 로그인 성공 시 호출
     * */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository, redisRefreshTokenService);
    }

    /*
     * 로그인 실패 시 호출
     * */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);

        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());

        return customJsonUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userRepository, redisRefreshTokenService);

        return jwtAuthenticationFilter;
    }
}