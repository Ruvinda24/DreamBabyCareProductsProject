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
public class InventoryDto extends ArrayList<InventoryDto> {

    private String inventory_id;
    private String item_name;
    private String printed_embroidered;
    private String size;
    private double unit_price;
    private int quantity_available;
    private String stored_location;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<InventoryDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<InventoryDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<InventoryDto> reversed() {
        return super.reversed();
    }

}
