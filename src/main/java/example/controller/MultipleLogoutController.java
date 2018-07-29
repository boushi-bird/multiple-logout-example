package example.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MultipleLogoutController {
  @Autowired
  private SessionRegistry sessionRegistry;

  @RequestMapping("/logoutAnother")
  public String logoutAnother(HttpSession currentSession) {
    String sessionId = currentSession.getId();
    Optional<Object> principal = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(authentication -> authentication.getPrincipal());
    if (principal.isPresent()) {
      // 現在ログインしているユーザと同一ユーザのセッション一覧を取得
      List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal.get(), false);
      for (SessionInformation session : sessions) {
        if (sessionId.equals(session.getSessionId())) {
          // 現在のセッションだけはログアウトしない
          continue;
        }
        // セッション期限切れにしてログアウト
        session.expireNow();
      }
    }
    return "redirect:/";
  }
}
