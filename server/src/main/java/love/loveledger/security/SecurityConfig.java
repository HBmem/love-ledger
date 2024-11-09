package love.loveledger.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/login", "logout").permitAll()
                .antMatchers("/add", "/edit/*").hasAnyRole("USER", "ADMIN")
                .antMatchers("/delete/*").hasAnyRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-err");

        http.sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/login?expired=true");
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
