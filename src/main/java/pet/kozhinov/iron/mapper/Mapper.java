package pet.kozhinov.iron.mapper;

public interface Mapper<T1, T2> {
    T2 map1(T1 from);
    T1 map2(T2 to);
}
