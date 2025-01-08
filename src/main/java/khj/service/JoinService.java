package khj.service;

import jakarta.servlet.http.HttpServletRequest;
import khj.model.Member;

public interface JoinService {

    Member join(HttpServletRequest req, Member member);

}
