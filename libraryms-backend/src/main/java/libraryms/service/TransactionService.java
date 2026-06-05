package libraryms.service;

import libraryms.model.*;
import libraryms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    // Issue a book to a member
    public Transaction issueBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // Business rule: can't borrow a book that's already borrowed
        if (!"available".equals(book.getStatus())) {
            throw new RuntimeException("Book is not available for borrowing");
        }

        // Create the transaction record
        Transaction txn = new Transaction();
        txn.setBook(book);
        txn.setMember(member);
        txn.setBorrowDate(LocalDate.now());
        txn.setDueDate(LocalDate.now().plusDays(14));    // 14-day loan
        txn.setStatus("borrowed");

        // Update the book status and increment safe count
        book.setStatus("borrowed");
        int currentCount = (book.getBorrowCount() == null) ? 0 : book.getBorrowCount();
        book.setBorrowCount(currentCount + 1);
        bookRepository.save(book);

        // Update member's total borrow count with safe null-check
        int currentMemberTotal = (member.getTotalBorrowed() == null) ? 0 : member.getTotalBorrowed();
        member.setTotalBorrowed(currentMemberTotal + 1);
        memberRepository.save(member);

        return transactionRepository.save(txn);
    }

    // Return a borrowed book
    public Optional<Transaction> returnBook(Long transactionId) {
        return transactionRepository.findById(transactionId).map(txn -> {
            txn.setStatus("returned");
            txn.setReturnDate(LocalDate.now());

            // Mark the book as available again
            Book book = txn.getBook();
            book.setStatus("available");
            bookRepository.save(book);

            return transactionRepository.save(txn);
        });
    }

    public List<Transaction> getOverdue() {
        return transactionRepository.findByStatusAndDueDateBefore(
                "borrowed", LocalDate.now()
        );
    }
}