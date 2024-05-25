package show.schedulemanagement.repository.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);

    @EntityGraph(attributePaths = {"roles"})
    @Query("select m from Member m where m.email = :email")
    Optional<Member> findWithRolesByEmail(@Param("email") String email);
}
