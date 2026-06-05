package libraryms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many transactions can reference one book
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")    // Foreign key column name
    private Book book;

    // Many transactions can reference one member
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;    // null until the book is returned

    // "borrowed" or "returned"
    private String status = "borrowed";
}
