package khj.config;

import jakarta.servlet.ServletContextEvent;


public interface InitApp {
    void init(String packageName, ServletContextEvent sce);
}
