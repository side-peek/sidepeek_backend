package sixgaezzang.sidepeek.like.service;

import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.validateLoginId;
import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.LIKE_IS_DUPLICATED;
import static sixgaezzang.sidepeek.like.exception.message.LikeErrorMessage.LIKE_NOT_EXISTING;
import static sixgaezzang.sidepeek.like.util.validation.LikeValidator.validateLikeId;
import static sixgaezzang.sidepeek.like.util.validation.LikeValidator.validateLikeRequest;
import static sixgaezzang.sidepeek.like.util.validation.LikeValidator.validateLoginIdEqualsLikeOwnerId;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.like.domain.Like;
import sixgaezzang.sidepeek.like.dto.request.LikeRequest;
import sixgaezzang.sidepeek.like.dto.response.LikeResponse;
import sixgaezzang.sidepeek.like.repository.LikeRepository;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.service.ProjectService;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.service.UserService;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final ProjectService projectService;

    @Transactional
    public LikeResponse save(Long loginId, LikeRequest request) {
        validateLoginId(loginId);
        validateLikeRequest(request);

        User user = userService.getById(loginId);

        Project project = projectService.getById(request.projectId());

        validateLikeExistence(user, project);

        Like like = Like.builder()
            .user(user)
            .project(project)
            .build();

        like = likeRepository.save(like);
        project.increaseLikeCount();    // 좋아요 수 증가

        return LikeResponse.from(like);
    }

    @Transactional
    public void delete(Long loginId, Long likeId) {
        validateLoginId(loginId);
        validateLikeId(likeId);

        Like like = getById(loginId, likeId);

        Project project = like.getProject();
        project.decreaseLikeCount();    // 좋아요 수 감소

        likeRepository.delete(like);
    }

    public Like getById(Long loginId, Long id) {
        Like like = likeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(LIKE_NOT_EXISTING));

        validateLoginIdEqualsLikeOwnerId(loginId,
            like.getUser().getId());  // 로그인한 사용자와 좋아요 Id한 사용자가 동일한지 확인

        return like;
    }

    private void validateLikeExistence(User user, Project project) {
        if (likeRepository.existsByUserAndProject(user, project)) {
            throw new EntityExistsException(LIKE_IS_DUPLICATED);
        }
    }
}
