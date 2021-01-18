package com.beertag.config;

import com.beertag.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;

    private AuthenticationManager authManager;

    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, userID"
                        + " from users where username=?")
                .authoritiesByUsernameQuery("select username, authority "
                        + "from authority where username=?")
                .passwordEncoder(new BCryptPasswordEncoder());

    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("admin").password("admin").authorities("ROLE_USER").and()
//                .withUser("javainuse").password("javainuse").authorities("ROLE_USER", "ROLE_ADMIN");    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        CharacterEncodingFilter filter = new CharacterEncodingFilter();
//        filter.setEncoding("UTF-8");
//        filter.setForceEncoding(true);
//        http.addFilterBefore(filter, CsrfFilter.class);
//        http
//                .authorizeRequests()
//                .antMatchers( "/sign-in", "/auth/**", "/api/**",
//                        "/home", "/", "/all_beers", "/register", "../resources/static/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/sign-in")
//                .defaultSuccessUrl("/",true).failureUrl("/sign-in?error=true").permitAll()
//                .and().logout().logoutSuccessUrl("/sign-in?logout=true")
//                .invalidateHttpSession(true).permitAll();
//    }

    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().sessionManagement().disable()
                .authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/sign-in").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/home/all_beers").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/").permitAll()
              //  .antMatchers("/create").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/**").permitAll()
               // .antMatchers("/user/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/sign-in")
                .defaultSuccessUrl("/home/all_beers", true)
                .failureUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("../css/**", "/scripts/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}