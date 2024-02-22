package sixgaezzang.sidepeek.projects.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.projects.domain.member.Member;
import sixgaezzang.sidepeek.projects.dto.request.MemberSaveRequest;
import sixgaezzang.sidepeek.projects.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void saveAll(Project project, List<MemberSaveRequest> memberSaveRequests) {
        List<Member> members = memberSaveRequests.stream().map(
            member -> Member.builder()
                .project(project)
                .nickname(member.nickname())
                .userId(member.userId())
                .role(member.role())
                .build()
        ).toList();

        memberRepository.saveAll(members);
    }
}
