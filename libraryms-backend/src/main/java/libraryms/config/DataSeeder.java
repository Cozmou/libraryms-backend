package libraryms.config;

import libraryms.model.*;
import libraryms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            AdminRepository adminRepo,
            BookRepository bookRepo,
            MemberRepository memberRepo
    ) {
        return args -> {
            // Create default admin (only if none exists)
            if (adminRepo.count() == 0) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword("admin123"); // In production, hash this with BCrypt
                admin.setName("Library Admin");
                adminRepo.save(admin);
                System.out.println("✅ Default admin created: admin / admin123");
            }

            // Seed some books
            if (bookRepo.count() == 0) {
                bookRepo.save(createBook("The Great Gatsby", "F. Scott Fitzgerald",
                        "978-0-7432-7356-5", "Fiction", 1925, 15));
                bookRepo.save(createBook("1984", "George Orwell",
                        "978-0-452-28423-4", "Dystopian Fiction", 1949, 31));
                bookRepo.save(createBook("Dune", "Frank Herbert",
                        "978-0-441-17271-9", "Science Fiction", 1965, 19));
                bookRepo.save(createBook("Pride and Prejudice", "Jane Austen",
                        "978-0-14-143951-8", "Romance", 1813, 18));
                System.out.println("✅ Sample books created");
            }

            // Seed some members
            if (memberRepo.count() == 0) {
                memberRepo.save(createMember("Alice Johnson",
                        "alice@email.com", "(555) 123-4567"));
                memberRepo.save(createMember("Bob Smith",
                        "bob@email.com", "(555) 987-6543"));
                System.out.println("✅ Sample members created");
            }
        };
    }

    private Book createBook(String title, String author, String isbn,
                            String category, int year, int borrows) {
        Book b = new Book();
        b.setTitle(title); b.setAuthor(author); b.setIsbn(isbn);
        b.setCategory(category); b.setPublishedYear(year); b.setBorrowCount(borrows);
        return b;
    }

    private Member createMember(String name, String email, String phone) {
        Member m = new Member();
        m.setName(name); m.setEmail(email); m.setPhone(phone);
        m.setMembershipDate(LocalDate.now()); m.setStatus("active");
        return m;
    }
}
