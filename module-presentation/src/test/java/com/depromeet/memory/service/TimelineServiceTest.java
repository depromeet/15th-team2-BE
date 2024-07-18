package com.depromeet.memory.service;

import com.depromeet.member.Member;
import com.depromeet.member.MemberRole;
import com.depromeet.memory.Memory;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.StrokeCreateRequest;
import com.depromeet.memory.mock.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;

public class TimelineServiceTest {
    private FakeMemoryRepository memoryRepository;
    private FakeMemoryDetailRepository memoryDetailRepository;
    private FakeMemberRepository memberRepository;
    private FakeAuthorizationUtil authorizationUtil;
    private FakePoolRepository poolRepository;
    private MemoryService memoryService;
    private FakeStrokeRepository strokeRepository;
    private StrokeService strokeService;
    private TimelineService timelineService;

    private Long userId = 1L; // 로그인한 사용자 아이디 임의 지정
    private Member member1;
    private Memory memory;
    private Integer expectedTotalMeter = 0;
    private Short lane;

    private static List<String> STROKE_NAME_LIST = List.of("자유형", "배영", "평형", "접영", "잠영");

    @BeforeEach
    void init() {
        // dependencies
        memoryRepository = new FakeMemoryRepository();
        memoryDetailRepository = new FakeMemoryDetailRepository();

        memberRepository = new FakeMemberRepository();
        authorizationUtil = new FakeAuthorizationUtil(userId);

        poolRepository = new FakePoolRepository();

        strokeRepository = new FakeStrokeRepository();
        strokeService = new StrokeServiceImpl(strokeRepository, memoryRepository);

        // member create
        member1 =
                Member.builder()
                        .id(userId)
                        .name("member1")
                        .email("member1@gmail.com")
                        .role(MemberRole.USER)
                        .build();
        memberRepository.save(member1);

        // memoryService
        memoryService =
                new MemoryServiceImpl(
                        memoryRepository,
                        memoryDetailRepository,
                        memberRepository,
                        authorizationUtil,
                        poolRepository);

        timelineService = new TimelineServiceImpl(memoryRepository);
        memory = saveMemory();
        lane = memory.getLane();
    }

    Memory saveMemory() {
        MemoryCreateRequest memoryCreateRequest =
                MemoryCreateRequest.builder()
                        .item("항공 모함")
                        .heartRate((short) 100)
                        .pace(LocalTime.of(1, 0))
                        .kcal(100)
                        .lane((short) 25)
                        .diary("test")
                        .recordAt(LocalDate.of(2024, 7, 15))
                        .startTime(LocalTime.of(15, 0))
                        .endTime(LocalTime.of(15, 50))
                        .build();
        return memoryService.save(memoryCreateRequest);
    }

    void saveMeterStroke() {
        Integer meter = 50;
        List<StrokeCreateRequest> scr = new ArrayList<>();
        for (int i = 0; i < STROKE_NAME_LIST.size(); i++) {
            scr.add(new StrokeCreateRequest(STROKE_NAME_LIST.get(i), null, meter));
            expectedTotalMeter += meter;
        }
        strokeService.saveAll(memory, scr);
    }

    void saveLapsStroke() {
        List<StrokeCreateRequest> scr = new ArrayList<>();
        Short laps = 2;
        for (int i = 0; i < STROKE_NAME_LIST.size(); i++) {
            scr.add(new StrokeCreateRequest(STROKE_NAME_LIST.get(i), laps, null));
            expectedTotalMeter += laps * lane;
        }
        strokeService.saveAll(memory, scr);
    }

    //    @DisplayName("memory -> TimelineResponseDto 변환 : stroke에 meter 저장")
    //    @Test
    //    void mapToTimelineResponseDtoStrokeMeterTest() {
    //        saveMeterStroke();
    //        TimelineResponseDto result = timelineService.mapToTimelineResponseDto(memory);
    //
    //        System.out.println(result);
    //        assertThat(result.totalMeter()).isEqualTo(expectedTotalMeter);
    //    }
    //
    //    @DisplayName("memory -> TimelineResponseDto 변환 : stroke에 meter 저장")
    //    @Test
    //    void mapToTimelineResponseDtoStrokeLapsTest() {
    //        saveLapsStroke();
    //        TimelineResponseDto result = timelineService.mapToTimelineResponseDto(memory);
    //
    //        System.out.println(result);
    //        assertThat(result.totalMeter()).isEqualTo(expectedTotalMeter);
    //    }
}