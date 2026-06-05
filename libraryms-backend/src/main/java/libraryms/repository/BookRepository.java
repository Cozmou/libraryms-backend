package libraryms.repository;

import libraryms.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByStatus(String status);
    // Spring translates this method name into:
    // SELECT * FROM books WHERE status = ?
}
