package com.ufcg.br.listapramim.seguranca;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.ufcg.br.listapramim.usuario.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	CustomUserDetailsService userService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    UserDetailsService userDetailsService = userService;
	    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.httpBasic().disable().csrf().disable().sessionManagement()
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
	            .antMatchers("/api/auth/login").permitAll()
	            .antMatchers("/api/auth/register").permitAll()
	            .antMatchers("/api/auth/changePassword").permitAll()
	            .antMatchers("/api/produto/**").hasAuthority("ADMIN").anyRequest().authenticated()
	            .antMatchers("/api/listacompra/**").hasAuthority("ADMIN").anyRequest().authenticated()
	            .and().csrf()
	            .disable().exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint()).and()
	            .apply(new JwtConfigurer(jwtTokenProvider));
	    
	}
	
	@Bean
	public PasswordEncoder bCryptPasswordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
	    return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
	            "Unauthorized");
	}
	
////	 Usado para adicionar role admin no db
//	@Bean
//	CommandLineRunner init(RoleRepository roleRepository) {
//	    return args -> {
//
//	        Role adminRole = roleRepository.findByRole("ADMIN");
//	        if (adminRole == null) {
//	            Role newAdminRole = new Role();
//	            newAdminRole.setRole("ADMIN");
//	            roleRepository.save(newAdminRole);
//	        }
//	    };
//
//	}
	
}