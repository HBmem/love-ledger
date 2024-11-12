package love.loveledger.domain;

import love.loveledger.data.NotableDayRepository;
import love.loveledger.models.NotableDay;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotableDayService {
    private final NotableDayRepository notableDayRepository;

    public NotableDayService(NotableDayRepository notableDayRepository) {
        this.notableDayRepository = notableDayRepository;
    }

    public List<NotableDay> findAll() {
        return notableDayRepository.findAll();
    }

    public NotableDay findById(int notableDayId) {
        return notableDayRepository.findById(notableDayId);
    }

    public Result<NotableDay> add(NotableDay notableDay) {
        Result<NotableDay> result = validate(notableDay);

        if (!result.isSuccess()) {
            return result;
        }

        if (notableDay.getNotableDayId() != 0) {
            result.addMessage("Notable day ID cannot be set for an 'add' operation", ResultType.INVALID);
            return result;
        }

        notableDay = notableDayRepository.add(notableDay);
        result.setPayload(notableDay);
        return result;
    }

    public Result<NotableDay> update(NotableDay notableDay) {
        Result<NotableDay> result = validate(notableDay);

        if (!result.isSuccess()) {
            return result;
        }

        if (notableDay.getNotableDayId() <= 0) {
            result.addMessage("Notable day ID must be set for an 'update' operation", ResultType.INVALID);
            return result;
        }

        if (!notableDayRepository.update(notableDay)) {
            String msg = String.format("Notable Day ID: %s, not found", notableDay.getNotableDayId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int notableDayId) { return notableDayRepository.delete(notableDayId);}

    private Result<NotableDay> validate(NotableDay notableDay) {
        Result<NotableDay> result = new Result<>();

        if (Validations.isNullOrBlank(notableDay.getName())) {
            result.addMessage("Name must not be null of blank", ResultType.INVALID);
        }

        if (notableDay.getDay() < 1 || notableDay.getDay() > 31) {
            result.addMessage("Day must be between 1 and 31", ResultType.INVALID);
        }

        if (notableDay.getMonth() < 1 || notableDay.getMonth() > 12) {
            result.addMessage("Month must ne between 1 and 12", ResultType.INVALID);
        }

        return result;
    }
}
