package main.java.khj.service;

import jakarta.servlet.http.HttpServletRequest;
import main.java.khj.annotation.CustomAutowired;
import main.java.khj.annotation.CustomBean;
import main.java.khj.config.BeanConfig;
import main.java.khj.repository.MemberRepository;
import main.java.khj.repository.MemberRepositoryImp;

@CustomBean("loginService")
public class LoginServiceImp implements LoginService {


    @CustomAutowired
    private MemberRepository memberRepository;


    @Override
    public boolean login(String id, String pw, HttpServletRequest req) {
        boolean result = false;
        System.out.println("LoginServiceImp access!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        // 의존성 주입 ---------------------------------------------------------------------------------
        BeanConfig beanConfig = (BeanConfig) req.getServletContext().getAttribute("beanConfig");
        System.out.println("beanConfig :: " + beanConfig);
        beanConfig.injectDependencies(this);
        // -------------------------------------------------------------------------------------------
        if(id == null || pw == null) {
            return false;
        }


        try{
            memberRepository.save(id,pw);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }
}
