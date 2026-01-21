package simple.automation;

import java.util.ArrayList;
import java.util.List;

public class Paginator<T> {
    private final List<T> items;
    private final int pageSize;

    public Paginator(List<T> items, int pageSize) {
        this.items = items;
        this.pageSize = pageSize;
    }

    public Page<T> getPage(int pageNumber) {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be greater than 0");
        }

        int totalPages = getTotalPages();
        if (pageNumber > totalPages) {
            return new Page<>(new ArrayList<>(), pageNumber, pageSize, items.size(), totalPages);
        }

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, items.size());

        List<T> pageItems = items.subList(startIndex, endIndex);
        return new Page<>(pageItems, pageNumber, pageSize, items.size(), totalPages);
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) items.size() / pageSize);
    }

    public int getTotalItems() {
        return items.size();
    }

    public static class Page<T> {
        private final List<T> items;
        private final int pageNumber;
        private final int pageSize;
        private final int totalItems;
        private final int totalPages;

        public Page(List<T> items, int pageNumber, int pageSize, int totalItems, int totalPages) {
            this.items = items;
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalItems = totalItems;
            this.totalPages = totalPages;
        }

        public List<T> getItems() {
            return items;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotalItems() {
            return totalItems;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public boolean hasNext() {
            return pageNumber < totalPages;
        }

        public boolean hasPrevious() {
            return pageNumber > 1;
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }
    }
}
