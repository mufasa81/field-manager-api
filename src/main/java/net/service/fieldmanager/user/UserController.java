package net.service.fieldmanager.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<User> createUser(@RequestBody UserCreationRequest request) throws ExecutionException, InterruptedException {
        User newUser = userService.createUser(request);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<String> login(@RequestBody User user) throws ExecutionException, InterruptedException {
        System.out.println("login called." + user.getEmail()+"/"+user.getPassword());
        String token = userService.login(user.getEmail(), user.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() throws ExecutionException, InterruptedException {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request) throws ExecutionException, InterruptedException {
        User updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
