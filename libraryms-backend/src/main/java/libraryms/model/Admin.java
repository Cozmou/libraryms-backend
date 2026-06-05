package libraryms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity                    // Marks this as a database table
@Table(name = "admins")    // Table will be named "admins"
@Data                      // Lombok: auto-generates getters, setters, toString
@NoArgsConstructor         // Lombok: generates empty constructor
@AllArgsConstructor        // Lombok: generates constructor with all fields
public class Admin {

    @Id                                                    // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)    // Auto-increment
    private Long id;

    @Column(unique = true, nullable = false)    // Must be unique and not null
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;
}