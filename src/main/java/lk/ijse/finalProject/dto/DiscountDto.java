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
public class DiscountDto extends ArrayList<DiscountDto> {

    private  String discount_id;
    private  String payment_id;
    private  String discount_type;
    private  double discount_percentage;


    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<DiscountDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<DiscountDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<DiscountDto> reversed() {
        return super.reversed();
    }

}
