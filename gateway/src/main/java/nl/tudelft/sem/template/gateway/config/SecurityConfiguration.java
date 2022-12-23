package nl.tudelft.sem.template.gateway.config;

import nl.tudelft.sem.template.gateway.authentication.JwtAuthenticationEntryPoint;
import nl.tudelft.sem.template.gateway.authentication.JwtRequestFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final transient JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final transient JwtRequestFilter jwtRequestFilter;

    public SecurityConfiguration(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                 JwtRequestFilter jwtRequestFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/user/hello").hasRole("EMPLOYEE")
                .and()
                .authorizeRequests().antMatchers("/userView/sysadmin").hasRole("SYSADMIN")
                .and()
                .authorizeRequests().antMatchers("/faculty/*").hasRole("FACULTY_REVIEWER")
                .and()
                .authorizeRequests()
                .antMatchers("/example/user", "/user/add", "/user/authenticate")
                .permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        //return http.build();
    }
}
