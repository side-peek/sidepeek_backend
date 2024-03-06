package sixgaezzang.sidepeek.like.repository;

import java.util.List;

public interface LikeRepositoryCustom {

    List<Long> findAllProjectIdsByUser(Long userId);

}
