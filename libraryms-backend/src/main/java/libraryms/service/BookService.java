package libraryms.service;

import libraryms.model.Book;
import libraryms.model.Transaction;
import libraryms.repository.BookRepository;
import libraryms.repository.TransactionRepository; // 1. Import your TransactionRepository
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 2. Import Transactional

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final TransactionRepository transactionRepository; // 3. Inject it here safely

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> update(Long id, Book updates) {
        return bookRepository.findById(id).map(existing -> {
            if (updates.getTitle()         != null) existing.setTitle(updates.getTitle());
            if (updates.getAuthor()        != null) existing.setAuthor(updates.getAuthor());
            if (updates.getIsbn()          != null) existing.setIsbn(updates.getIsbn());
            if (updates.getCategory()      != null) existing.setCategory(updates.getCategory());
            if (updates.getDescription()   != null) existing.setDescription(updates.getDescription());
            if (updates.getPublishedYear() != null) existing.setPublishedYear(updates.getPublishedYear());
            if (updates.getStatus()        != null) existing.setStatus(updates.getStatus());
            return bookRepository.save(existing);
        });
    }

    // 4. Wrap with @Transactional to ensure both deletions succeed together or fail together cleanly
    @Transactional
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Business Rule: Safety block so admins don't accidentally delete a book currently out on loan
        if ("borrowed".equals(book.getStatus())) {
            throw new RuntimeException("Cannot delete a book that is currently borrowed and out on loan.");
        }

        // 5. Query and delete all dependent transactions from your history logs first
        List<Transaction> attachedTransactions = transactionRepository.findAll().stream()
                .filter(t -> t.getBook() != null && t.getBook().getId().equals(id))
                .toList();

        if (!attachedTransactions.isEmpty()) {
            transactionRepository.deleteAll(attachedTransactions);
        }

        // 6. Now that the road is clear, delete the parent book profile safely
        bookRepository.delete(book);
    }
}
