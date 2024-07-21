package com.depromeet.memory.mock;

import com.depromeet.memory.Memory;
import com.depromeet.memory.repository.MemoryRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public class FakeMemoryRepository implements MemoryRepository {
    private Long autoGeneratedId = 0L;
    private final List<Memory> data = new ArrayList<>();

    @Override
    public Memory save(Memory memory) {
        if (memory.getId() == null || memory.getId() == 0) {
            Memory newMemory =
                    Memory.builder()
                            .id(++autoGeneratedId)
                            .member(memory.getMember())
                            .pool(memory.getPool())
                            .memoryDetail(memory.getMemoryDetail())
                            .recordAt(memory.getRecordAt())
                            .startTime(memory.getStartTime())
                            .endTime(memory.getEndTime())
                            .lane(memory.getLane())
                            .diary(memory.getDiary())
                            .build();
            data.add(newMemory);
            return newMemory;
        } else {
            data.removeIf(item -> item.getId().equals(memory.getId()));
            data.add(memory);
            return memory;
        }
    }

    @Override
    public Optional<Memory> findById(Long memoryId) {
        return data.stream().filter(item -> item.getId().equals(memoryId)).findAny();
    }

    @Override
    public Optional<Memory> update(Long memoryId, Memory memoryUpdate) {
        Optional<Memory> md = data.stream().filter(item -> item.getId().equals(memoryId)).findAny();
        if (md.isEmpty()) {
            return Optional.empty();
        } else {
            Memory origin = md.get();
            return Optional.of(
                    Memory.builder()
                            .id(memoryId)
                            .member(origin.getMember())
                            .pool(
                                    memoryUpdate.getPool() != null
                                            ? memoryUpdate.getPool()
                                            : origin.getPool())
                            .memoryDetail(
                                    memoryUpdate.getMemoryDetail() != null
                                            ? memoryUpdate.getMemoryDetail()
                                            : origin.getMemoryDetail())
                            .strokes(
                                    memoryUpdate.getStrokes() != null
                                            ? memoryUpdate.getStrokes()
                                            : origin.getStrokes())
                            .images(
                                    memoryUpdate.getImages() != null
                                            ? memoryUpdate.getImages()
                                            : origin.getImages())
                            .recordAt(
                                    memoryUpdate.getRecordAt() != null
                                            ? memoryUpdate.getRecordAt()
                                            : origin.getRecordAt())
                            .startTime(
                                    memoryUpdate.getStartTime() != null
                                            ? memoryUpdate.getStartTime()
                                            : origin.getStartTime())
                            .endTime(
                                    memoryUpdate.getEndTime() != null
                                            ? memoryUpdate.getEndTime()
                                            : origin.getEndTime())
                            .lane(
                                    memoryUpdate.getLane() != null
                                            ? memoryUpdate.getLane()
                                            : origin.getLane())
                            .diary(
                                    memoryUpdate.getDiary() != null
                                            ? memoryUpdate.getDiary()
                                            : origin.getDiary())
                            .build());
        }
    }

    @Override
    public Slice<Memory> findPrevMemoryByMemberId(
            Long memberId,
            Long cursorId,
            LocalDate cursorRecordAt,
            Pageable pageable,
            LocalDate recordAt) {
        return null;
    }

    @Override
    public Slice<Memory> findNextMemoryByMemberId(
            Long memberId,
            Long cursorId,
            LocalDate cursorRecordAt,
            Pageable pageable,
            LocalDate recordAt) {
        return null;
    }
}
