package sixgaezzang.sidepeek.like.service;

import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginId;
import static sixgaezzang.sidepeek.like.util.validation.LikeValidator.validateLikeRequest;
import static sixgaezzang.sidepeek.projects.exception.message.ProjectErrorMessage.PROJECT_NOT_EXISTING;
import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.like.domain.Like;
import sixgaezzang.sidepeek.like.dto.request.LikeRequest;
import sixgaezzang.sidepeek.like.dto.response.LikeResponse;
import sixgaezzang.sidepeek.like.repository.LikeRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.repository.project.ProjectRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public LikeResponse toggle(Long loginId, LikeRequest request) {

        validateLoginId(loginId);
        validateLikeRequest(request);

        User user = userRepository.findById(loginId)
            .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXISTING));

        Project project = projectRepository.findById(request.projectId())
            .orElseThrow(() -> new EntityNotFoundException(PROJECT_NOT_EXISTING));

        Optional<Like> existingLike = likeRepository.findByUserAndProject(user, project);

        if (existingLike.isPresent()) {  // 좋아요 삭제(취소)
            likeRepository.delete(existingLike.get());
            return new LikeResponse(false);
        }

        Like like = Like.builder()  // 좋아요 생성
            .user(user)
            .project(project)
            .build();

        likeRepository.save(like);

        return new LikeResponse(true);
    }
}
