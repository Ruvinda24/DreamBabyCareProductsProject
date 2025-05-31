package lk.ijse.finalProject.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ProductionTaskDto extends ArrayList<ProductionDto> {

    private String production_task_id;
    private String production_id;
    private String task_id;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<ProductionDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<ProductionDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<ProductionDto> reversed() {
        return super.reversed();
    }
}
