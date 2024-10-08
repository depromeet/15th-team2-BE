package com.depromeet.memory.api;

import com.depromeet.config.log.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.memory.dto.request.MemoryCreateRequest;
import com.depromeet.memory.dto.request.MemoryUpdateRequest;
import com.depromeet.memory.dto.response.*;
import com.depromeet.memory.facade.MemoryFacade;
import com.depromeet.type.memory.MemorySuccessType;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memory")
public class MemoryController implements MemoryApi {
    private final MemoryFacade memoryFacade;

    @PostMapping
    @Logging(item = "Memory", action = "POST")
    public ApiResponse<MemoryCreateResponse> create(
            @LoginMember Long memberId,
            @Valid @RequestBody MemoryCreateRequest memoryCreateRequest) {
        MemoryCreateResponse response = memoryFacade.create(memberId, memoryCreateRequest);
        return ApiResponse.success(MemorySuccessType.POST_RESULT_SUCCESS, response);
    }

    @GetMapping("/{memoryId}")
    @Logging(item = "Memory", action = "GET")
    public ApiResponse<MemoryResponse> read(
            @LoginMember Long memberId, @PathVariable("memoryId") Long memoryId) {
        MemoryResponse response = memoryFacade.findById(memberId, memoryId);
        return ApiResponse.success(MemorySuccessType.GET_RESULT_SUCCESS, response);
    }

    @GetMapping("/{memoryId}/edit-data")
    @Logging(item = "Memory", action = "GET")
    public ApiResponse<MemoryReadUpdateResponse> readForUpdate(
            @LoginMember Long memberId, @PathVariable("memoryId") Long memoryId) {
        MemoryReadUpdateResponse response = memoryFacade.getMemoryForUpdate(memberId, memoryId);
        return ApiResponse.success(MemorySuccessType.GET_RESULT_SUCCESS, response);
    }

    @PatchMapping("/{memoryId}")
    @Logging(item = "Memory", action = "PATCH")
    public ApiResponse<MemoryResponse> update(
            @LoginMember Long memberId,
            @PathVariable("memoryId") Long memoryId,
            @Valid @RequestBody MemoryUpdateRequest request) {
        MemoryResponse response = memoryFacade.update(memberId, memoryId, request);
        return ApiResponse.success(MemorySuccessType.PATCH_RESULT_SUCCESS, response);
    }

    @GetMapping("/timeline")
    @Logging(item = "Memory", action = "GET")
    public ApiResponse<TimelineSliceResponse> timeline(
            @LoginMember Long memberId,
            @RequestParam(name = "cursorRecordAt", required = false)
                    @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate cursorRecordAt) {
        TimelineSliceResponse result =
                memoryFacade.getTimelineByMemberIdAndCursorAndDate(memberId, cursorRecordAt);
        return ApiResponse.success(MemorySuccessType.GET_TIMELINE_SUCCESS, result);
    }

    @GetMapping("/calendar")
    @Logging(item = "Memory", action = "GET")
    public ApiResponse<CalendarResponse> getCalendar(
            @LoginMember Long requesterId,
            @RequestParam("year") Integer year,
            @RequestParam("month") Short month,
            @RequestParam(value = "targetId", required = false) Long targetId) {
        CalendarResponse response = memoryFacade.getCalendar(requesterId, targetId, year, month);
        return ApiResponse.success(MemorySuccessType.GET_CALENDAR_SUCCESS, response);
    }

    @DeleteMapping("/{memoryId}")
    @Logging(item = "Memory", action = "DELETE")
    public ApiResponse<?> delete(
            @LoginMember Long memberId, @PathVariable("memoryId") Long memoryId) {
        memoryFacade.deleteById(memberId, memoryId);
        return ApiResponse.success(MemorySuccessType.DELETE_SUCCESS);
    }

    @GetMapping("/last")
    public ApiResponse<LastMemoryResponse> getLast(@LoginMember Long memberId) {
        LastMemoryResponse response = memoryFacade.getLastMemoryByMemberId(memberId);
        return ApiResponse.success(MemorySuccessType.GET_LAST_SUCCESS, response);
    }
}
