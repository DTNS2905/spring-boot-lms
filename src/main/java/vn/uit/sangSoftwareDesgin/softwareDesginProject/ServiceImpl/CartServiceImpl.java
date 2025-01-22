package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO.CourseItemDTO;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Entity.*;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Exception.CartNotFoundException;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Exception.CourseNotFoundException;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Exception.UserNotFoundException;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.CartRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.CourseRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Repo.UserRepo;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CartService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private CartRepo cartRepository;

    @Autowired
    private CourseRepo courseRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Fetches all cart items for a given user and calculates total quantity and price.
     *
     * @param username The username of the user whose cart is being processed.
     * @return A map containing the total quantity, total price, and detailed cart items.
     */
    @Override
    public Map<String, Object> getCartSummary(String username) {
        // Fetch user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        // Check if the user has a cart
        Cart cart = user.getCart();
        if (cart == null || cart.getCartCourses().isEmpty()) {
            throw new CartNotFoundException("No items found in the cart for user: " + username);
        }

        // Calculate total quantity and total price
        int totalQuantity = cart.getCartCourses().stream()
                .mapToInt(CartCourse::getQuantity)
                .sum();

        // Calculate total price
        BigDecimal totalPrice = cart.getCartCourses().stream()
                .map(item -> item.getCourse().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // Prepare cart details
        List<Map<String, Object>> cartDetails = cart.getCartCourses().stream()
                .map(item -> {
                    Map<String, Object> cartItemDetails = new HashMap<>();
                    cartItemDetails.put("courseId", item.getCourse().getId());
                    cartItemDetails.put("courseName", item.getCourse().getTitle());
                    cartItemDetails.put("price", item.getCourse().getPrice());
                    cartItemDetails.put("quantity", item.getQuantity());
                    cartItemDetails.put("subtotal", item.getCourse().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                    return cartItemDetails;
                })
                .toList();


        // Build and return the response
        return Map.of(
                "totalQuantity", totalQuantity,
                "totalPrice", totalPrice,
                "cartItems", cartDetails
        );
    }

    @Override
    public CartCourse getCartItem(String username, String courseId) {
        // Fetch user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        // Check if the user has a cart
        Cart cart = user.getCart();
        if (cart == null || cart.getCartCourses().isEmpty()) {
            throw new CartNotFoundException("No items found in the cart for user: " + username);
        }

        // Search for the course in the cart
        return cart.getCartCourses().stream()
                .filter(item -> item.getCourse().getId().toString().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new CourseNotFoundException("Course with ID " + courseId + " not found in the cart."));
    }


    @Override
    public List<Course> getAllCoursesInCart(String username) {
        // Fetch user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        // Check if the user has a cart
        Cart cart = user.getCart();
        if (cart == null || cart.getCartCourses().isEmpty()) {
            throw new CartNotFoundException("No items found in the cart for user: " + username);
        }

        // Retrieve all courses from the cart
        return cart.getCartCourses().stream()
                .map(CartCourse::getCourse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<Course> addCoursesToCart(String username, CourseItemDTO courseItemDTO) {
        // Fetch user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        // Get the user's cart, or create one if it doesn't exist
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
        }

        // Initialize the list of added courses
        List<Course> addedCourses = new ArrayList<>();

        // Fetch courses using IDs from CourseItemDTO
        for (Long courseId : courseItemDTO.getCourseID()) { // Adjusted to fetch IDs from DTO
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + courseId));

            // Check if the course is already in the cart
            boolean alreadyInCart = cart.getCartCourses().stream()
                    .anyMatch(item -> item.getCourse().getId().equals(course.getId()));

            if (!alreadyInCart) {
                // Add course to cart
                CartCourse cartItem = new CartCourse();
                cartItem.setCart(cart);
                cartItem.setCourse(course);
                cartItem.setQuantity(1); // Assuming quantity is always 1 for courses

                cart.getCartCourses().add(cartItem);
                addedCourses.add(course);
            } else {
                throw new RuntimeException("Course already added to the cart.");
            }
        }

        // Save the updated cart
        cartRepository.save(cart);
        return addedCourses;
    }


    @Transactional
    @Override
    public List<Course> removeCoursesFromCart(String username, CourseItemDTO courseItemDTO) {
        // Fetch user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        // Get the user's cart
        Cart cart = user.getCart();
        if (cart == null || cart.getCartCourses().isEmpty()) {
            throw new CartNotFoundException("No items found in the cart for user: " + username);
        }

        // Remove courses from the cart
        List<Course> removedCourses = new ArrayList<>();

        for (Long courseId : courseItemDTO.getCourseID()) { // Adjusted to fetch IDs from DTO
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + courseId));

            // Find the course in the cart
            CartCourse cartItem = cart.getCartCourses().stream()
                    .filter(item -> item.getCourse().getId().equals(course.getId()))
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {
                cart.getCartCourses().remove(cartItem);
                removedCourses.add(course);
            }
            else {
                throw new CourseNotFoundException("can not delete course with ID: " + courseId);
            }
        }

        cartRepository.save(cart); // Save the updated cart
        return removedCourses;
    }


    /**
     * Fetches courses from the user's cart by specific IDs.
     */
    public List<Course> getCoursesByIdsInCart(String username, List<Long> courseIds) {
        // Fetch the cart for the user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        Cart cart = user.getCart();
        if (cart == null || cart.getCartCourses().isEmpty()) {
            throw new CartNotFoundException("No items found in the cart for user: " + username);
        }

        // Filter courses in the cart by the specified IDs
        return cart.getCartCourses().stream()
                .map(CartCourse::getCourse)
                .filter(course -> courseIds.contains(course.getId()))
                .toList();
    }



}
