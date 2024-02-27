package sixgaezzang.sidepeek.projects.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.member.Member;
import sixgaezzang.sidepeek.projects.dto.request.MemberSaveRequest;
import sixgaezzang.sidepeek.projects.dto.response.MemberSummary;
import sixgaezzang.sidepeek.projects.repository.MemberRepository;
import sixgaezzang.sidepeek.users.domain.User;
import sixgaezzang.sidepeek.users.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveAll(Project project, List<MemberSaveRequest> memberSaveRequests) {
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
                    .orElseThrow(() -> new EntityNotFoundException("User Id에 해당하는 회원이 없습니다."));

                return memberBuilder.user(user)
                    .build();
            }
        ).toList();

        memberRepository.saveAll(members);
    }

    public List<MemberSummary> findAllWithUser(Project project) {
        return memberRepository.findAllWithUser(project);
    }
}
