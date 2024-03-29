package sixgaezzang.sidepeek.users.service;

import static sixgaezzang.sidepeek.common.util.validation.TechStackValidator.validateTechStacks;
import static sixgaezzang.sidepeek.common.util.validation.ValidationUtils.isNullOrEmpty;
import static sixgaezzang.sidepeek.skill.exception.message.SkillErrorMessage.SKILL_NOT_EXISTING;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateUser;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.common.dto.request.SaveTechStackRequest;
import sixgaezzang.sidepeek.skill.domain.Skill;
import sixgaezzang.sidepeek.skill.repository.SkillRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.domain.UserSkill;
import sixgaezzang.sidepeek.users.dto.response.UserSkillSummary;
import sixgaezzang.sidepeek.users.repository.userskill.UserSkillRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserSkillService {

    private final UserSkillRepository userSkillRepository;
    private final SkillRepository skillRepository;

    public List<UserSkillSummary> findAllByUser(User user) {
        validateUser(user);

        return userSkillRepository.findAllByUser(user)
            .stream()
            .map(UserSkillSummary::from)
            .toList();
    }

    public List<UserSkillSummary> saveAll(User user, List<SaveTechStackRequest> techStacks) {
        validateUser(user);
        if (userSkillRepository.existsByUser(user)) {
            userSkillRepository.deleteAllByUser(user);
        }

        if (isNullOrEmpty(techStacks)) {
            return Collections.emptyList();
        }

        validateTechStacks(techStacks);
        List<UserSkill> skills = convertAllToEntity(user, techStacks);
        userSkillRepository.saveAll(skills);

        return skills.stream()
            .map(UserSkillSummary::from)
            .toList();
    }

    private List<UserSkill> convertAllToEntity(User user, List<SaveTechStackRequest> techStacks) {
        return techStacks.stream()
            .map(techStack -> {
                Skill skill = skillRepository.findById(techStack.skillId())
                    .orElseThrow(() -> new EntityNotFoundException(SKILL_NOT_EXISTING));

                return techStack.toUserSkill(user, skill);
            })
            .toList();
    }
}
