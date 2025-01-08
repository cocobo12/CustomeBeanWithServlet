package khj.service;

import jakarta.servlet.http.HttpServletRequest;
import khj.model.Member;

public interface LoginService {

    Member login(HttpServletRequest req, Member member);
}
