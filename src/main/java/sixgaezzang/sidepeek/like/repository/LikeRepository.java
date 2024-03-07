package sixgaezzang.sidepeek.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sixgaezzang.sidepeek.like.domain.Like;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {

}
