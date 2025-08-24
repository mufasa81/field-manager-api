package net.service.fieldmanager.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Data Loader running...");
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//        user.setName("Test User");
//        user.setRole(Role.User);
//        user.setActive(true);
//        userRepository.save(user);
    }
}
