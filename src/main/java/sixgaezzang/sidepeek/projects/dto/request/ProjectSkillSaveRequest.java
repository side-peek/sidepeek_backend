package sixgaezzang.sidepeek.projects.dto.request;

public record ProjectSkillSaveRequest(
    Long projectId,
    Long skillId,
    String category
) {
}
