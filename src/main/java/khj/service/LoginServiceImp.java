package main.java.khj.service;

import jakarta.servlet.http.HttpServletRequest;
import main.java.khj.annotation.CustomAutowired;
import main.java.khj.annotation.CustomBean;
import main.java.khj.annotation.CustomComponent;
import main.java.khj.config.BeanConfig;
import main.java.khj.model.Member;
import main.java.khj.repository.MemberRepository;
import main.java.khj.repository.MemberRepositoryImp;


@CustomComponent("loginService")
public class LoginServiceImp implements LoginService {


    @CustomAutowired
    private MemberRepository memberRepository;


    @Override
    public boolean login(HttpServletRequest req, Member member) {
        boolean result = false;
        System.out.println("LoginServiceImp access!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        req.getServletContext().getAttribute("components");
        if (member == null) {
            return false;
        }


        try {
            memberRepository.save(member);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
