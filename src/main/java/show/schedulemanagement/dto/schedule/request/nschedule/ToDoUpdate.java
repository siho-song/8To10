package show.schedulemanagement.dto.schedule.request.nschedule;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToDoUpdate {
    @NotNull
    private Long id;
    private boolean isComplete;

    public boolean isSameId(Long id){
        return this.id.equals(id);
    }
}
