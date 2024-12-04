package mediHub_be.security.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import mediHub_be.security.filter.CustomAuthenticationFilter;
import mediHub_be.security.filter.JwtFilter;
import mediHub_be.security.handler.JwtAccessDeniedHandler;
import mediHub_be.security.handler.JwtAuthenticationEntryPoint;
import mediHub_be.security.handler.LoginFailureHandler;
import mediHub_be.security.handler.LoginSuccessHandler;
import mediHub_be.security.util.JwtUtil;
import mediHub_be.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final Environment env;
    private final JwtUtil jwtUtil;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        /* CSRF 토큰 발행 시 Client에서 매번 해당 토큰도 함께 요청에 넘겨 주어야 하므로 기능 비 활성화 */
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz ->
                        authz.requestMatchers(new AntPathRequestMatcher("/api/v1/user", "post")).permitAll()
                                .anyRequest().authenticated()
                )
                /* session 로그인 방식을 사용하지 않음 (JWT Token 방식을 사용할 예정) */
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        /* 커스텀 로그인 필터 이전에 JWT 토큰 확인 필터를 설정 */
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        /* 커스텀 로그인 필터 추가 */
        http.addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        /* 인증, 인가 실패 핸들러 설정 */
        http.exceptionHandling(
                exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(new JwtAccessDeniedHandler());
                    exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                }
        );

        /* CORS 설정 */
        http.cors(cors -> cors
                .configurationSource(corsConfigurationSource()));

        return http.build();
    }

    private Filter getAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(getAuthenticationManager());
        customAuthenticationFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(env));
        customAuthenticationFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        return customAuthenticationFilter;
    }

    private AuthenticationManager getAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return new ProviderManager(provider);
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); // 허용할 도메인
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        config.addExposedHeader("token"); // 서버측에서 보내는 헤더에 대한 허용 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}