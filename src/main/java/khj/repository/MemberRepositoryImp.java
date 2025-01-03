package main.java.khj.repository;

import main.java.khj.annotation.CustomBean;

import java.util.HashMap;
import java.util.Map;

@CustomBean("memberRepository")
public class MemberRepositoryImp implements MemberRepository {

    public static Map<String, String> member = new HashMap<>();

    public MemberRepositoryImp() {}

    @Override
    public void save(String id, String pw) {
        // 로그인 정보 db에 저장
        member.put(id, pw);
    }
}
