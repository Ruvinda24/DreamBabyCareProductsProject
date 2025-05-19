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
public class EmployeeDto extends ArrayList<EmployeeDto> {
    private  String employee_id;
    private  String name;
    private String nic;
    private String number;
    private String role;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<EmployeeDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<EmployeeDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<EmployeeDto> reversed() {
        return super.reversed();
    }

}
