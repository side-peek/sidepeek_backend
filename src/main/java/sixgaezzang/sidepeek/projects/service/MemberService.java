package sixgaezzang.sidepeek.projects.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static sixgaezzang.sidepeek.projects.util.validation.MemberValidator.validateMembers;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateProject;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.member.Member;
import sixgaezzang.sidepeek.projects.dto.request.SaveMemberRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.repository.member.MemberRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.dto.response.UserSummary;
import sixgaezzang.sidepeek.users.service.UserService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserService userService;

    @Transactional
    public List<MemberSummary> cleanAndSaveAll(Project project,
        List<SaveMemberRequest> memberSaveRequests) {
        validateProject(project);
        validateMembers(project.getOwnerId(), memberSaveRequests);

        cleanExistingMembersByProject(project);

        List<Member> members = memberSaveRequests.stream()
            .map(member -> member.toEntity(
                project,
                Objects.nonNull(member.id()) ? userService.getById(member.id()) : null)
            )
            .toList();

        return generateMemberSummaries(memberRepository.saveAll(members));
    }

    public List<MemberSummary> findAll(Project project) {
        List<Member> members = memberRepository.findAllByProject(project);

        return generateMemberSummaries(members);
    }

    public Optional<User> findFellowMemberByProject(Long userId, Project project) {
        return memberRepository.findAllByProject(project)
            .stream()
            .filter(member -> Objects.equals(member.getUserId(), userId))
            .findAny()
            .map(Member::getUser);
    }

    private void cleanExistingMembersByProject(Project project) {
        if (memberRepository.existsByProject(project)) {
            memberRepository.deleteAllByProject(project);
        }
    }

    private List<MemberSummary> generateMemberSummaries(List<Member> members) {
        Map<String, List<UserSummary>> memberMap = members.stream()
            .collect(groupingBy(Member::getRole,
                mapping(member -> UserSummary.of(member.getUser(), member.getNickname()),
                    toList())));

        return memberMap.entrySet().stream()
            .map(entry -> MemberSummary.of(entry.getKey(), entry.getValue()))
            .toList();
    }
}
