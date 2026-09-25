package io.luck.DotaTrainerBackend.mapper;

import java.util.Collection;

public interface Mapper<T,R> {

     T convert(R item);

    Collection<T> convert(Collection<R> items);


}
