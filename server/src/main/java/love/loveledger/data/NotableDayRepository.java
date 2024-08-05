package love.loveledger.data;

import love.loveledger.models.NotableDay;

import java.util.List;

public interface NotableDayRepository {
    List<NotableDay> findAll();

    NotableDay findById(int notableDayId);

    NotableDay add(NotableDay notableDay);

    boolean update(NotableDay notableDay);

    boolean delete(int notableDayId);
}
