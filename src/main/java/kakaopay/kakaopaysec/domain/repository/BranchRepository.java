package kakaopay.kakaopaysec.domain.repository;

import kakaopay.kakaopaysec.domain.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    Branch findByBrNm(String brNm);
}
