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
public class CustomerDto extends ArrayList<CustomerDto> {
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String orderPlatForm;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<CustomerDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<CustomerDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<CustomerDto> reversed() {
        return super.reversed();
    }
}
