package sixgaezzang.sidepeek.users.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSkillSummary;
import sixgaezzang.sidepeek.users.repository.userskill.UserSkillRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserSkillService {

    private final UserSkillRepository userSkillRepository;

    public List<UserSkillSummary> findAllByUser(User user) {
        return userSkillRepository.findAllByUser(user)
            .stream()
            .map(UserSkillSummary::from)
            .toList();
    }

}
