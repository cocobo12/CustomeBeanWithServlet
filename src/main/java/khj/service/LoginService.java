package main.java.khj.service;

import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {

    boolean login(String id, String pw, HttpServletRequest req);
}
