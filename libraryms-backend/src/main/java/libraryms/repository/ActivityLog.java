package libraryms.repository; // <-- Check that this is line 1!

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Data
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String details;
    private LocalDateTime timestamp;
}