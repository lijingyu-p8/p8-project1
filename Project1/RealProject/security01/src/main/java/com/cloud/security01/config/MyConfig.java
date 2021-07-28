package com.cloud.security01.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		// 忽略静态请求
		web.ignoring().antMatchers("/img/**", "/js/**");
	}

	/**
	 * 自定义登陆界面
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated()
		.and()
		    //退出
		    .logout().logoutUrl("/logout.html").addLogoutHandler(new LogoutHandler() {
				
				@Override
				public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
				}
			})
		    .and()
		    //登陆
		    .formLogin().loginPage("/login.html").
				// 自定义参数标识
				usernameParameter("username").passwordParameter("password")
				// 成功、失败的页面
				.loginProcessingUrl("login").failureUrl(".fail.html").defaultSuccessUrl("/").permitAll()
				.failureHandler(new AuthenticationFailureHandler() {

					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException exception) throws IOException, ServletException {

					}
				})
				//登陆成功的处理
				.successHandler(new AuthenticationSuccessHandler() {
					
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						
					}
				}).
				and()
				// csrf校验
				.csrf().csrfTokenRepository(new HttpSessionCsrfTokenRepository());
		// 记住我功能，会生成remember-me，并加上过期时间
		http.rememberMe().and()
				// 只允许一台登陆，并且账号不退出，其他设备无法登陆
				.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true)
		;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new MyUserDetailsService());
		auth.authenticationProvider(new MyAuthprovider());
		auth.inMemoryAuthentication().withUser("456").password("456").roles("admin").and().withUser("555")
				.password("555").roles("");
		JdbcUserDetailsManager userDetailsService = auth.jdbcAuthentication().dataSource(dataSource)
				.getUserDetailsService();

	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Autowired
	private DataSource dataSource;

	@Bean
	protected UserDetailsService userDetailsService() {
		// jdbc方式配置用户
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
		if (manager.userExists("admin")) {
			System.out.println("已注册");
		} else {
			// 可以内置管理员
			manager.createUser(User.withUsername("admin").password(new BCryptPasswordEncoder().encode("123"))
					.roles("admin").build());
		}
		return manager;
	}
}
