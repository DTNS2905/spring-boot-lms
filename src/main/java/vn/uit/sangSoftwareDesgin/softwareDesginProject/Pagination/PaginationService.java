package vn.uit.sangSoftwareDesgin.softwareDesginProject.Pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Pagination.ApiResponse;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Pagination.ApiResponse.Pagination;

import java.util.function.Function;

@Service
public class PaginationService {

    /**
     * Generic pagination method.
     *
     * @param repositoryCall Function to call the repository for paginated data.
     * @param pageable       Pageable object containing pagination info.
     * @param mapper         Function to map the entity to DTO (if needed).
     * @param <E>            Entity type.
     * @param <D>            DTO type (or use the same as E if no transformation is needed).
     * @return ApiResponse with paginated data and metadata.
     */
    public <E, D> ApiResponse<?> getPaginatedResponse(
            Function<Pageable, Page<E>> repositoryCall,
            Pageable pageable,
            Function<E, D> mapper
    ) {
        // Fetch the paginated data from the repository
        Page<E> page = repositoryCall.apply(pageable);

        // Transform entities to DTOs (if needed)
        var content = page.map(mapper).getContent();

        // Create pagination metadata
        Pagination pagination = Pagination.of(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );

        // Return the response
        return ApiResponse.success("Fetched data successfully", content, pagination);
    }
}
