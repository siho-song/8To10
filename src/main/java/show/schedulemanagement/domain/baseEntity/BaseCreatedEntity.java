package show.schedulemanagement.domain.baseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseCreatedEntity {
    @CreatedDate
    @Column(updatable = false , nullable = false)
    private LocalDateTime createAt;

    //TODO Spring Security 적용 후 구현
//    @CreatedBy
//    @Column(updatable = false , nullable = false)
//    private String createdBy;
}
