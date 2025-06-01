package lk.ijse.finalProject.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class ShipmentDto extends ArrayList<ShipmentDto> {

    private String shipment_id;
    private String tracking_number;
    private LocalDate shipment_date;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<ShipmentDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<ShipmentDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<ShipmentDto> reversed() {
        return super.reversed();
    }

}
