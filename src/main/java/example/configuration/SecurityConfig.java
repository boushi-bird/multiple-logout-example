package example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/secure.html").authenticated().anyRequest().permitAll()
        // login settings
        .and().formLogin()
        // ligout settings
        .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
        // ここから追加分
        .and().sessionManagement()
        // 1ユーザあたりの許容する最大セッション数
        // -1だと無制限
        .maximumSessions(-1)
        // 複数ログアウトに使用するSessionRegistry
        .sessionRegistry(sessionRegistry(null))
        // セッション切れになった場合に遷移するURL(設定しなかった場合、その画面でエラーメッセージが出る)
        .expiredUrl("/login?logout");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    PasswordEncoder encoder = passwordEncoder();
    auth.inMemoryAuthentication().passwordEncoder(encoder).withUser("user").password(encoder.encode("password"))
        .roles("USER");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ConfigureRedisAction configureRedisAction() {
    return ConfigureRedisAction.NO_OP;
  }

  @Bean
  public <S extends Session> SessionRegistry sessionRegistry(FindByIndexNameSessionRepository<S> sessionRepository) {
    return new SpringSessionBackedSessionRegistry<>(sessionRepository);
  }
}
