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

public class UserDto extends ArrayList<UserDto> {
    private String user_id;
    private  String user_name;
    private  String password;
    private  String email;
    private  String name;
    private  String phone_number;

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return super.toArray(generator);
    }

    @Override
    public Stream<UserDto> stream() {
        return super.stream();
    }

    @Override
    public Stream<UserDto> parallelStream() {
        return super.parallelStream();
    }

    @Override
    public List<UserDto> reversed() {
        return super.reversed();
    }
}

