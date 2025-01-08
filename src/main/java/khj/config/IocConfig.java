package khj.config;

import jakarta.servlet.ServletContextEvent;
import khj.annotation.CustomBean;
import khj.repository.MemberRepositoryImp;
import khj.service.JoinServiceImp;
import khj.service.LoginServiceImp;


public class IocConfig {

    @CustomBean("memberRepository")
    MemberRepositoryImp memberRepository = new MemberRepositoryImp();

    @CustomBean("loginService")
    LoginServiceImp loginService = new LoginServiceImp();

    @CustomBean("joinService")
    JoinServiceImp joinService = new JoinServiceImp();
}
