package net.service.fieldmanager.user;

import net.service.fieldmanager.security.JwtTokenProvider;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public User createUser(UserCreationRequest request) throws ExecutionException, InterruptedException {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // In a real application, you should encrypt the password
        if (request.getRole() == null) {
            user.setRole(Role.User); // Assign a default role
        } else {
            user.setRole(request.getRole());
        }
        user.setActive(request.isActive());
        user.setCreatedDate(java.time.Instant.now()); // Set createdDate explicitly
        return userRepository.save(user);
    }

    public String login(String email, String password) throws ExecutionException, InterruptedException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return jwtTokenProvider.createToken(email, user.getName(), user.getRole());
    }

    public Optional<User> getUserById(String id) throws ExecutionException, InterruptedException {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        return userRepository.findAll();
    }

    public User updateUser(String id, UserUpdateRequest request) throws ExecutionException, InterruptedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            user.setPassword(request.getPassword()); // In a real application, encrypt the password
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        user.setActive(request.isActive());

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
