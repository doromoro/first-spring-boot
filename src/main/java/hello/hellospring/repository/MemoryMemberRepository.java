package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class MemoryMemberRepository implements MemberRepository{

    // 실무에서는 공유되는 메모리의 동시성 문제에 의해서 뭘 써야하는데 그냥 예제니까 진행?
    private static Map<Long, Member> store = new HashMap<>();

    private static long sequence = 0L;


    @Override
    public Member save(Member member) {
        // 이러면 1번부터 쓰게 될텐데?
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // null 처리를 위해서 optional로 감싸서 반환
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        // 람다를 사용한다고 함. 자바 강의는 아니니 그냥 자연스럽게 사용해버리시네
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();

    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
