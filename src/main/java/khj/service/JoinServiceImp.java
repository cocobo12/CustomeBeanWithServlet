package khj.service;

import jakarta.servlet.http.HttpServletRequest;
import khj.annotation.CustomAutowired;
import khj.model.Member;
import khj.repository.MemberRepository;

public class JoinServiceImp implements JoinService {
    @CustomAutowired
    private MemberRepository memberRepository;


    @Override
    public Member join(HttpServletRequest req, Member member) {
        Member result = null;
        System.out.println("LoginServiceImp access!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        req.getServletContext().getAttribute("components");
        if (member == null) {
            return result;
        }


        try {
            Member member1 = memberRepository.save(member);
            //req.getSession().setAttribute("email", member1.getEmail());
            result = member1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
