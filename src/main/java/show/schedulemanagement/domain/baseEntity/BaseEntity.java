package show.schedulemanagement.domain.baseEntity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{
    //TODO Spring Security 적용 후 구현
//    @CreatedBy
//    @Column(updatable = false , nullable = false)
//    private String createdBy;
//
//    @LastModifiedBy
//    @Column(nullable = false)
//    private String updatedBy;
}
