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

public class TaskDto extends ArrayList<TaskDto> {

    private String task_id;
    private String employee_id;
    private String description;
    private String status;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<TaskDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<TaskDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<TaskDto> reversed() {
        return super.reversed();
    }

}
