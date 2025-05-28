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
public class PaymentDto extends ArrayList<PaymentDto> {

    private String payment_id;
    private String order_id;
    private double amount;
    private String payment_method;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<PaymentDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<PaymentDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<PaymentDto> reversed() {
        return super.reversed();
    }

}
