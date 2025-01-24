package vn.uit.sangSoftwareDesgin.softwareDesginProject.Pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    // Method to get pagination as a convenience
    // Method to attach pagination dynamically
    @Getter
    @Setter
    private Pagination pagination; // Nullable by default

    // Constructor with pagination
    public ApiResponse(String status, String message, T data, Pagination pagination) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }

    // Constructor without pagination
    public ApiResponse(String status, String message, T data) {
        this(status, message, data, null); // No pagination by default
    }

    // Static method for success response
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    public static <T> ApiResponse<T> success(String message, T data, Pagination pagination) {
        return new ApiResponse<>("success", message, data, pagination);
    }

    // Static method for error response
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>("error", message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
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


        public static Pagination of(int pageNumber, int pageSize, long totalElements) {
            int totalPages = (int) Math.ceil((double) totalElements / pageSize);
            boolean isLastPage = pageNumber == totalPages - 1;

            return new Pagination(pageNumber, pageSize, totalElements, totalPages, isLastPage);
        }

        @Override
        public String toString() {
            return String.format(
                    "Pagination [pageNumber=%d, pageSize=%d, totalElements=%d, totalPages=%d, isLastPage=%b]",
                    pageNumber, pageSize, totalElements, totalPages, isLastPage
            );
        }
    }
}
