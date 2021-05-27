package pet.kozhinov.iron.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pet.kozhinov.iron.entity.dto.CaseDto;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class Utils {

    public static <T> Page<T> toPage(Collection<T> collection) {
        return new PageImpl<>(new LinkedList<>(collection));
    }

    public static <T> Page<T> toPage(Collection<T> collection, int page, int size) {
        List<T> paged = collection.stream()
                .skip((long) (page-1) * size)
                .limit(size)
                .collect(Collectors.toList());
        return new PageImpl<>(paged, PageRequest.of(page, size), collection.size());
    }

    private Utils() {}
}
