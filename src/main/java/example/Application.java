package example;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
  @RequestMapping("/user")
  public Principal user(Principal principal) {
    return principal;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
