package main.java.khj.service;

import jakarta.servlet.http.HttpServletRequest;
import main.java.khj.model.Member;

public interface LoginService {

    boolean login(HttpServletRequest req, Member member);
}
