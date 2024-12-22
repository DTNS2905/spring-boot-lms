package vn.uit.sangSoftwareDesgin.softwareDesginProject.ResponseInstance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Pagination pagination; // Nullable by default

    public ApiResponse(String status, String message, T data, Pagination pagination) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }

    public ApiResponse(String status, String message, T data) {
        this(status, message, data, null); // No pagination by default
    }

    @Getter
    @Setter
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean isLastPage;

        public Pagination(int pageNumber, int pageSize, long totalElements, int totalPages, boolean isLastPage) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.isLastPage = isLastPage;
        }
    }
}
