package show.schedulemanagement.domain.auditing.baseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{
    @CreatedBy
    @Column(updatable = false , nullable = false)
    protected String createdBy;

    @LastModifiedBy
    @Column(nullable = false)
    protected String updatedBy;
}
