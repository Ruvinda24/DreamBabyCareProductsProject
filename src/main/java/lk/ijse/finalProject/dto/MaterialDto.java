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
public class MaterialDto extends ArrayList<MaterialDto> {

    private  String material_id;
    private  String name;
    private  String color_type;
    private int quantity;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<MaterialDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<MaterialDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<MaterialDto> reversed() {
        return super.reversed();
    }

}
