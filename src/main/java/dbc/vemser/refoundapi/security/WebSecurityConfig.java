package dbc.vemser.refoundapi.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenService tokenService;
    private final AuthenticationService authenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable().and().cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/**/").permitAll()
                .antMatchers("/user/saveUser/**").permitAll()
                .antMatchers("/user/saveAdmin/**").hasRole("ADMIN")
                .antMatchers("/user/updateUser/**").hasAnyRole("ADMIN", "FINANCEIRO", "GESTOR","COLABORADOR")
                .antMatchers("/user/updateAdmin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/item/**").hasRole("COLABORADOR")
                .antMatchers(HttpMethod.POST,"/item/**").hasRole("COLABORADOR")
                .antMatchers(HttpMethod.PUT,"/refund/").hasRole("COLABORADOR")
                .antMatchers(HttpMethod.GET,"/item/**").hasAnyRole("ADMIN", "FINANCEIRO", "GESTOR","COLABORADOR")
                .antMatchers(HttpMethod.GET,"/refund/**").hasAnyRole("ADMIN", "FINANCEIRO", "GESTOR","COLABORADOR")
                .antMatchers(HttpMethod.PUT,"/refund/updateStatus").hasAnyRole("FINANCEIRO", "GESTOR")
                .antMatchers(HttpMethod.POST,"/refund/**").hasRole("COLABORADOR")
                .antMatchers(HttpMethod.DELETE,"/refund/**").hasAnyRole("COLABORADOR","ADMIN")
                .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
