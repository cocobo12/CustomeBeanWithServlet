package main.java.khj.config;

import jakarta.servlet.ServletContextEvent;
import main.java.khj.annotation.CustomBean;
import main.java.khj.annotation.CustomConfiguration;
import main.java.khj.repository.MemberRepositoryImp;
import main.java.khj.service.LoginServiceImp;


public class IocConfig {


    @CustomBean("memberRepository")
    MemberRepositoryImp memberRepository = new MemberRepositoryImp();

    @CustomBean("loginService")
    LoginServiceImp loginService = new LoginServiceImp();


}
