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

public class MaterialUsageDto extends ArrayList<MaterialUsageDto> {

    private String usage_id;
    private String production_id;
    private String material_id;
    private int quantity_used;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<MaterialUsageDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<MaterialUsageDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<MaterialUsageDto> reversed() {
        return super.reversed();
    }

}
