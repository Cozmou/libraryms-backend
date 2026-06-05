package libraryms.controller;

import libraryms.repository.dto.TransactionRequest;
import libraryms.model.Transaction;
import libraryms.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAll() {
        return transactionService.findAll();
    }

    // Issue a book: POST /api/transactions
    // Body: { "bookId": 1, "memberId": 2 }
    @PostMapping
    public ResponseEntity<?> issue(@RequestBody TransactionRequest req) {
        try {
            Transaction txn = transactionService.issueBook(req.bookId(), req.memberId());
            return ResponseEntity.status(201).body(txn);
        } catch (RuntimeException e) {
            // Return 400 Bad Request with the error message
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Return a book: POST /api/transactions/5/return
    @PostMapping("/{id}/return")
    public ResponseEntity<Transaction> returnBook(@PathVariable Long id) {
        return transactionService.returnBook(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/overdue")
    public List<Transaction> getOverdue() {
        return transactionService.getOverdue();
    }
}
