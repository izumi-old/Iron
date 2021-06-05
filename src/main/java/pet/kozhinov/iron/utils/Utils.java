package pet.kozhinov.iron.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class Utils {
    public static <T> Page<T> toPage(Collection<T> collection) {
        return new PageImpl<>(new LinkedList<>(collection));
    }

    public static <T> Page<T> toPage(Collection<T> collection, int page, int size) {
        return toPage(collection, PageRequest.of(page, size));
    }

    public static <T> Page<T> toPage(Collection<T> collection, Pageable pageable) {
        List<T> paged = collection.stream()
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        return new PageImpl<>(paged, pageable, collection.size());
    }

    private Utils() {}
}
