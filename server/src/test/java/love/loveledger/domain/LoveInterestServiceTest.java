package love.loveledger.domain;

import love.loveledger.data.LoveInterestRepository;
import love.loveledger.models.Gender;
import love.loveledger.models.LoveInterest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class LoveInterestServiceTest {
    @Autowired
    LoveInterestService service;

    @MockBean
    LoveInterestRepository loveInterestRepository;

    @Test
    void shouldAddNickname() {
        LoveInterest loveInterest = makeLoveInterest();
        loveInterest.setFirstName(null);
        loveInterest.setLastName(null);
        loveInterest.setNickname("DJ");

        LoveInterest mockOut = makeLoveInterest();
        mockOut.setFirstName(null);
        mockOut.setLastName(null);
        mockOut.setNickname("DJ");
        mockOut.setLoveInterestId(4);

        when(loveInterestRepository.add(loveInterest)).thenReturn(mockOut);

        Result<LoveInterest> actual = service.add(loveInterest);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldAddFirstName() {
        LoveInterest loveInterest = makeLoveInterest();

        LoveInterest mockOut = makeLoveInterest();
        mockOut.setLoveInterestId(4);

        when(loveInterestRepository.add(loveInterest)).thenReturn(mockOut);

        Result<LoveInterest> actual = service.add(loveInterest);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        LoveInterest loveInterest = makeLoveInterest();

        // Should not add when id is not 0
        loveInterest.setLoveInterestId(99);
        Result<LoveInterest> actual = service.add(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when nickname and firstname is null
        loveInterest.setLoveInterestId(0);
        loveInterest.setNickname(null);
        loveInterest.setFirstName(null);
        actual = service.add(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when birthday is tomorrow
        loveInterest = makeLoveInterest();
        LocalDate future = LocalDate.now();
        future = future.plusYears(1);
        loveInterest.setBirthday(future);
        actual = service.add(loveInterest);
        assertEquals(ResultType.INVALID, actual.getType());

        // Should not add when userId does not exist
        loveInterest = makeLoveInterest();
        loveInterest.setUserId(99);

//        when()

//        actual = service.add(loveInterest);
//        assertEquals(ResultType.INVALID, actual.getType());
    }

    private LoveInterest makeLoveInterest() {
        LoveInterest loveInterest = new LoveInterest();

        loveInterest.setFirstName("Donald");
        loveInterest.setLastName("Trump");
        loveInterest.setGender(Gender.MALE);
        loveInterest.setProfileImageUrl("IMG/URL");
        loveInterest.setBirthday(LocalDate.of(1996, 6, 14));
        loveInterest.setHobbies("lying;doing crimes;getting arrested");
        loveInterest.setLikes("lies;crimes;fascism");
        loveInterest.setDislikes("honesty;democracy");
        loveInterest.setUserId(2);

        return loveInterest;
    }
}