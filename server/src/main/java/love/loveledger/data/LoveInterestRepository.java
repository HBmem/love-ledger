package love.loveledger.data;

import love.loveledger.models.LoveInterest;

import java.util.List;

public interface LoveInterestRepository {
    List<LoveInterest> findAllLoveInterestByUserId(int userId);

    LoveInterest findLoveInterestById(int loveInterestId);

    LoveInterest add(LoveInterest loveInterest);

    boolean update(LoveInterest loveInterest);

    boolean delete(int loveInterestId);
}
