package main.java.khj.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;


public interface InitApp {
    void init(String packageName, ServletContextEvent sce);
}
