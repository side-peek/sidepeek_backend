package sixgaezzang.sidepeek.comments.domain;

import static sixgaezzang.sidepeek.comments.exception.message.CommentErrorMessage.IS_ANONYMOUS_IS_NULL;
import static sixgaezzang.sidepeek.comments.util.validation.CommentValidator.validateCommentContent;
import static sixgaezzang.sidepeek.comments.util.validation.CommentValidator.validateIsAnonymous;
import static sixgaezzang.sidepeek.projects.util.validation.ProjectValidator.validateProject;
import static sixgaezzang.sidepeek.users.util.validation.UserValidator.validateUser;

import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sixgaezzang.sidepeek.comments.dto.request.UpdateCommentRequest;
import sixgaezzang.sidepeek.common.domain.BaseTimeEntity;
import sixgaezzang.sidepeek.projects.domain.Project;
import sixgaezzang.sidepeek.users.domain.User;

@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {

    public static final Boolean DEFAULT_IS_ANONYMOUS = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // TODO: @OnDelete(action = OnDeleteAction.CASCADE) 설정을 안해도 돌아간다. 필요할까!(고민중)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(name = "is_anonymous", nullable = false)
    private boolean isAnonymous = DEFAULT_IS_ANONYMOUS;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder
    public Comment(Project project, User user, Comment parent, Boolean isAnonymous,
        String content) {
        validateConstructorRequiredArguments(project, user, isAnonymous, content);

        this.project = project;
        this.user = user;
        this.parent = parent;
        this.isAnonymous = isAnonymous;
        this.content = content;
    }

    private static void validateConstructorRequiredArguments(Project project, User user, Boolean isAnonymous,
                                                             String content) {
        validateProject(project);
        validateUser(user);
        validateIsAnonymous(isAnonymous);
        validateCommentContent(content);
    }

    // TODO: 업데이트 메서드는 request 인자를 풀어서 넣으면 더 확장성이 있다고 할 수 있을까?
    public void update(UpdateCommentRequest request) {
        setIsAnonymous(request.isAnonymous());
        setContent(request.content());
    }

    private void setIsAnonymous(Boolean isAnonymous) {
        Assert.notNull(isAnonymous, IS_ANONYMOUS_IS_NULL);
        this.isAnonymous = isAnonymous;
    }

    private void setContent(String content) {
        validateCommentContent(content);
        this.content = content;
    }

}
