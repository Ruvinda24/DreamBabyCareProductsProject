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

public class OrderItemDto extends ArrayList<OrderItemDto> {

    private String order_item_id;
    private String order_id;
    private String inventory_id;
    private int quantity;
    private double amount;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<OrderItemDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<OrderItemDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<OrderItemDto> reversed() {
        return super.reversed();
    }

}
