package main.java.khj.repository;

import main.java.khj.annotation.CustomBean;
import main.java.khj.annotation.CustomComponent;
import main.java.khj.model.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CustomComponent("memberRepository")
public class MemberRepositoryImp implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    private static final MemberRepository instance = new MemberRepositoryImp();

//    public static MemberRepository getInstance() {
//        return instance;
//    }

    public MemberRepositoryImp() {
    }

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
