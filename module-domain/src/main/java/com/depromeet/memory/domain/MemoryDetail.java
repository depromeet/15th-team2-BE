package com.depromeet.memory.domain;

import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemoryDetail {
    private Long id;
    private String item;
    private Short heartRate;
    private LocalTime pace;
    private Integer kcal;

    @Builder
    public MemoryDetail(Long id, String item, Short heartRate, LocalTime pace, Integer kcal) {
        this.id = id;
        this.item = item;
        this.heartRate = heartRate;
        this.pace = pace;
        this.kcal = kcal;
    }
}
