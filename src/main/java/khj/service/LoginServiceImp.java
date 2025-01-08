package khj.service;

import jakarta.servlet.http.HttpServletRequest;
import khj.annotation.CustomAutowired;
import khj.annotation.CustomComponent;
import khj.model.Member;
import khj.repository.MemberRepository;


@CustomComponent("loginService")
public class LoginServiceImp implements LoginService {


    @CustomAutowired
    private MemberRepository memberRepository;


    @Override
    public Member login(HttpServletRequest req, Member member) {
        Member result = null;
        //req.getServletContext().getAttribute("components");
        if (member == null) {
            return result;
        }

        try {
            Member member1 = memberRepository.findByEmailWithEqualsPassword(member);
            result = member1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
