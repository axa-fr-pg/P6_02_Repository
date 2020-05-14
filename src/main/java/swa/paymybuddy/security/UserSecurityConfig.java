package swa.paymybuddy.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()												// Implement Cross Site Request Forgery later on
			.authorizeRequests().antMatchers("/register**").permitAll()		// No authentication to access register page
			.and().authorizeRequests().antMatchers("/login**").permitAll()	// No authentication to access login page
			.and().formLogin()
					.loginPage("/login.html")								// Authentication with custom login page
					.loginProcessingUrl("/login")							// URL definition for login controller
					.failureUrl("/login-retry.html")
					.defaultSuccessUrl("/welcome.html")
			.and().rememberMe().key("mySecretTokenKey")						// Manage "Remember me" login
					.tokenRepository(tokenRepository())						// Store tokens on server database
					.userDetailsService(userDetailsService)					// Use our method to read credentials
			.and().authorizeRequests().anyRequest().authenticated()			// All requests require an authentication
			.and().httpBasic();
	}

	@Bean // This method is used during login to check if the password is correct
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean // This method is used to store tokens when remember me is checked prior to login
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl=new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}
}
