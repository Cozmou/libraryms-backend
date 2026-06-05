package libraryms.repository;

import libraryms.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByStatus(String status);
    List<Transaction> findByMemberId(Long memberId);
    List<Transaction> findByBookId(Long bookId);
    // Find all overdue borrowed books
    List<Transaction> findByStatusAndDueDateBefore(String status, java.time.LocalDate date);
}
