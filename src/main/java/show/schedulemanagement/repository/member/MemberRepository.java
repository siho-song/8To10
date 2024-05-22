package show.schedulemanagement.repository.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
}
