package libraryms.repository;

import libraryms.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<ModelClass, PrimaryKeyType>
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Spring auto-generates: findById, findAll, save, delete, etc.
    // You just declare the method signature for custom queries:
    Optional<Admin> findByUsername(String username);
}
