package show.schedulemanagement.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Result<T> {
    private List<T> items = new ArrayList<>();

    public static <E, T> Result<T> fromElementsGroup(List<E> elementsGroup, Function<E, List<T>> mapper) {
        Result<T> aggregatedResult = new Result<>();
        List<T> items = aggregatedResult.getItems();

        elementsGroup.forEach(group -> {
            List<T> elements = mapper.apply(group);
            items.addAll(elements);
        });

        return aggregatedResult;
    }

    public static <T, E> Result<T> fromElements(List<E> sourceElements, Function<E, T> mapper) {
        Result<T> result = new Result<>();
        List<T> items = result.getItems();

        sourceElements.forEach(sourceElement -> items.add(mapper.apply(sourceElement)));
        return result;
    }
}
