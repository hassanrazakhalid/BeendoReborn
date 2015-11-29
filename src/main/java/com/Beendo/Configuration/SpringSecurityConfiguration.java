package com.Beendo.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Beendo.Security.HierarchicalJsr250Voter;
import com.Beendo.Services.UserService;
import com.Beendo.Entities.User;

/*@Configuration
@EnableWebSecurity*/
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter{

	
	@Autowired
    private UserDetailsService userDetailsService;
	 
	private AuthenticationManagerBuilder  _auth;
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			
		auth
        .userDetailsService(userDetailsService);
			;
      /*  auth
            .inMemoryAuthentication()
                .withUser("superadmin").password("password").roles("SUPER_ADMIN");*/
		/*auth
			.authenticationProvider(customAuthenticationProvider());*/
	
			_auth = auth;
	}
	
	@Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return _auth.build();
    }
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	      		 
		 http
	            .authorizeRequests()
	                .antMatchers("/javax.faces.resource/**").permitAll()
	                .antMatchers("/Views/Unsecured/Login/*").permitAll()
	                .antMatchers("/Views/Unsecured/**").permitAll()
	                .antMatchers("/Views/Secured/Dashboard/*").permitAll()
	                .antMatchers("/Views/Secured/User/*").hasAnyRole("ADMIN,SUPER_ADMIN")
	                .antMatchers("/Views/Secured/User/**").hasAnyRole("ADMIN,SUPER_ADMIN")
	                .and()
	                	.formLogin()
	                	.loginPage("/Views/Unsecured/Login/index.xhtml")
	                	.permitAll()
	                .and()
	                .csrf().disable()
	            // Example Remember Me Configuration
	           ;
	    }
	
	@Bean
    AuthenticationProvider customAuthenticationProvider() {
		CustomAuthenticationProvider impl = new CustomAuthenticationProvider();
//        impl.setUserDetailsService(customUserDetailsService());
        /* other properties etc */
        return impl ;
    }
	
/*		@Bean(name="myAuthenticationManager")
	   @Override
	   public AuthenticationManager authenticationManagerBean() throws Exception {
	       return super.authenticationManagerBean();
	   }*/
	
	/*@Bean   
    UserDetailsService customUserDetailsService() {
         custom UserDetailsService code here 
		
		return new UserService();
    }*/
	
/*	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.and()
			.httpBasic();
	}*/
	
	@Bean
	public RoleHierarchyImpl roleHierarchy(){
		
		RoleHierarchyImpl hiberachyImpl = new RoleHierarchyImpl();
		hiberachyImpl.setHierarchy("ROLE_ADMIN > ROLE_USER"
								 + " ROLE_USER > ROLE_GUEST");
		return hiberachyImpl;
	}
	
	@Autowired
	@Bean
	public HierarchicalJsr250Voter roleVoter(RoleHierarchy roleHierarchy){
		
		HierarchicalJsr250Voter hieVoter = new HierarchicalJsr250Voter(roleHierarchy);
		return hieVoter;
	}
	
	@Bean
	public RoleVoter simpleRoleVoter(){
		
		RoleVoter role = new RoleVoter();
		
		return role;
	}
	
/*	@Autowired
	@Bean
	public UnanimousBased accessDecisionManager(RoleVoter simpleRoleVoter){
		
		List<AccessDecisionVoter<? extends Object>> voterList = new ArrayList<>();
		voterList.add(simpleRoleVoter);
		
		UnanimousBased unamis = new UnanimousBased(voterList);
		return unamis;
	}*/
}
