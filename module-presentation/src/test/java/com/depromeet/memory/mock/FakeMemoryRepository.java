package com.depromeet.memory.mock;

import com.depromeet.memory.Memory;
import com.depromeet.memory.repository.MemoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}