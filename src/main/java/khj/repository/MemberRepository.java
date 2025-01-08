package khj.repository;


import khj.model.Member;

public interface MemberRepository {
    Member save(Member member);

    Member findByEmailWithEqualsPassword(Member member);

}
