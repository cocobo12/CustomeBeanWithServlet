package main.java.khj.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface Controller {

    String process(Map<String, String> paramMap, Map<String, Object> model, HttpServletRequest req);
}
