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

public class SupplyDto extends ArrayList<SupplyDto> {

    private String supply_id;
    private String supplier_id;
    private String material_id;
    private double amount;
    private int quantity;


    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<SupplyDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<SupplyDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<SupplyDto> reversed() {
        return super.reversed();
    }

}
