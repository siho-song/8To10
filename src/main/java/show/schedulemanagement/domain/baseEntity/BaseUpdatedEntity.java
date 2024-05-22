package show.schedulemanagement.domain.baseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass
public abstract class BaseUpdatedEntity {
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //TODO Spring Security 적용 후 구현
//    @LastModifiedBy
//    @Column(name = "updated_by", nullable = false)
//    private String updatedBy;
}
