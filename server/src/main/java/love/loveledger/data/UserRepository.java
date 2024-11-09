package love.loveledger.data;

import love.loveledger.models.AppUser;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository {
    @Transactional
    AppUser findByUserId(int userId);

    @Transactional
    AppUser findByUsername(String username);

    @Transactional
    AppUser add(AppUser user);

    @Transactional
    void update(AppUser user);
}
