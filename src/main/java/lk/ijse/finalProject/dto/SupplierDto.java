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

public class SupplierDto extends ArrayList<SupplierDto> {

    private String supplier_id;
    private String name;
    private String contact;
    private String account_details;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<SupplierDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<SupplierDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<SupplierDto> reversed() {
        return super.reversed();
    }



}
