package sixgaezzang.sidepeek.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.users.repository.UserRepository;
import sixgaezzang.sidepeek.users.repository.userskill.UserSkillRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserSkillService {

    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;

}
