package libraryms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")    // Validation
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    private String isbn;
    private String category;
    private String description;
    private Integer publishedYear;

    // "available", "borrowed", or "reserved"
    private String status = "available";

    private Integer borrowCount = 0;
}
