package todo.tutorials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.tutorials.model.MerchantProfile;

public interface MerchantProfileRepository extends JpaRepository<MerchantProfile, Long> {
}

