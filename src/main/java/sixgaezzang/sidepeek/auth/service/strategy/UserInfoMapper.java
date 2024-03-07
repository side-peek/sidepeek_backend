package sixgaezzang.sidepeek.auth.service.strategy;

import java.util.Map;
import sixgaezzang.sidepeek.users.domain.User;

public interface UserInfoMapper {

    User mapToUser(Map<String, Object> attributes);

    String getProviderId(Map<String, Object> attributes);

}
