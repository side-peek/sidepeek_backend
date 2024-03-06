package sixgaezzang.sidepeek.projects.service;

import static sixgaezzang.sidepeek.users.exception.message.UserErrorMessage.USER_NOT_EXISTING;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.member.Member;
import sixgaezzang.sidepeek.projects.dto.request.SaveMemberRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.repository.MemberRepository;
import sixgaezzang.sidepeek.projects.util.validation.MemberValidator;
import sixgaezzang.sidepeek.projects.util.validation.ProjectValidator;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<MemberSummary> saveAll(Project project, List<SaveMemberRequest> memberSaveRequests) {
        ProjectValidator.validateProject(project);
        MemberValidator.validateMembers(project.getOwnerId(), memberSaveRequests);

        if (memberRepository.existsByProject(project)) {
            memberRepository.deleteAllByProject(project);
        }

        List<Member> members = memberSaveRequests.stream().map(
            member -> {
                Member.MemberBuilder memberBuilder = Member.builder()
                    .project(project)
                    .nickname(member.nickname())
                    .role(member.role());

                if (Objects.isNull(member.userId())) {
                    return memberBuilder.build();
                }

                User user = userRepository.findById(member.userId())
                    .orElseThrow(() -> new EntityNotFoundException(USER_NOT_EXISTING));

                return memberBuilder.user(user)
                    .build();
            }
        ).toList();
        memberRepository.saveAll(members);

        return members.stream()
            .map(MemberSummary::from)
            .toList();
    }

    public List<MemberSummary> findAllWithUser(Project project) {
        return memberRepository.findAllWithUser(project);
    }

    public Optional<User> findFellowMemberByProject(Long userId, Project project) {
        return memberRepository.findAllByProject(project)
            .stream()
            .filter(member -> Objects.equals(member.getUser().getId(), userId))
            .findAny()
            .map(Member::getUser);
    }
}
