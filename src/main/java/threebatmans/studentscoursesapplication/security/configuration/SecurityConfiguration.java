package threebatmans.studentscoursesapplication.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import threebatmans.studentscoursesapplication.security.service.PasswordEncoder;
import threebatmans.studentscoursesapplication.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Value("${spring.h2.console.enabled:true}")
    private boolean h2ConsoleEnabled;

    @Value("${spring.h2.console.path:/h2-console}")
    private String h2ConsolePath;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {


        auth    .userDetailsService(userDetailsService)
                .passwordEncoder(PasswordEncoder.getInstance());

    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {


        httpSecurity    .authorizeRequests()
                .antMatchers(

                        "/login",
                        "/logout",
                        "/js/**",
                        "/css/**",
                        "/images/**",
                        "/style.css",
                        "/asset/**"
                )
                .permitAll();

        if (h2ConsoleEnabled) {
            httpSecurity
                    .authorizeRequests()
                    .antMatchers(h2ConsolePath + "/**")
                    .permitAll();
            httpSecurity
                    .csrf().disable();
            httpSecurity
                    .headers().frameOptions().disable();
        }
//        http    .authorizeRequests()
//                    .antMatchers("/blogEntry/edit/**","/blogEntry/edit/process","/blogEntry/delete/**")
//                    .access("hasAnyRole('AUTHENTICATED_USER', 'ROLE_ADMIN')");


        httpSecurity
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll();

        httpSecurity
                .authorizeRequests()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403");

        httpSecurity
                .logout();

    }
}
