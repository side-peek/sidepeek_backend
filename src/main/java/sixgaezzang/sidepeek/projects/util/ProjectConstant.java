package sixgaezzang.sidepeek.projects.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectConstant {
    // Project
    public static final int MAX_PROJECT_NAME_LENGTH = 50;
    public static final int MAX_OVERVIEW_LENGTH = 300;
    public static final String YEAR_MONTH_PATTERN = "yyyy-MM";

    // Member
    public static final int MAX_MEMBER_COUNT = 10;
    public static final int MAX_ROLE_LENGTH = 15;

    // OverviewImage(File)
    public static final int MAX_OVERVIEW_IMAGE_COUNT = 6;

}
