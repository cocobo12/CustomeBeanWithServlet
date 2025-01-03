package main.java.khj.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;


public interface InitApp<T> {
    void init(T t, ServletContextEvent sce);
}
