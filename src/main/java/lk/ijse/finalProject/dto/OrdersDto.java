package lk.ijse.finalProject.dto;


import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class OrdersDto extends ArrayList<OrdersDto> {

    private String order_id;
    private LocalDate order_date;
    private String customer_id;
    private String shipment_id;
    private String status;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<OrdersDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<OrdersDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<OrdersDto> reversed() {
        return super.reversed();
    }

}
