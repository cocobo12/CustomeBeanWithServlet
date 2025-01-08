package khj.repository;


import khj.annotation.CustomComponent;
import khj.model.Member;

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

    @Override
    public Member findByEmailWithEqualsPassword(Member member) {
        return store.values().stream()
                .filter(mem -> member.getEmail().equals(member.getEmail()))  // email이 같은지 확인
                .filter(mem -> member.getPw().equals(member.getPw()))        // pw가 같은지 확인
                .findFirst()                                        // 첫 번째 일치하는 회원을 반환
                .orElse(null);                                      // 없으면 null 반환
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
