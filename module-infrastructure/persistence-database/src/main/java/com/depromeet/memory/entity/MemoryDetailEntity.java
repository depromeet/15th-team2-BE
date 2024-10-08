package com.depromeet.memory.entity;

import com.depromeet.memory.domain.MemoryDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryDetailEntity {
    @Id
    @Column(name = "memory_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;

    private Short heartRate;

    private LocalTime pace;

    private Integer kcal;

    @Builder
    public MemoryDetailEntity(Long id, String item, Short heartRate, LocalTime pace, Integer kcal) {
        this.id = id;
        this.item = item;
        this.heartRate = heartRate;
        this.pace = pace;
        this.kcal = kcal;
    }

    public static MemoryDetailEntity from(MemoryDetail memoryDetail) {
        if (memoryDetail == null) return null;

        return MemoryDetailEntity.builder()
                .id(memoryDetail.getId())
                .item(memoryDetail.getItem())
                .heartRate(memoryDetail.getHeartRate())
                .pace(memoryDetail.getPace())
                .kcal(memoryDetail.getKcal())
                .build();
    }

    public MemoryDetail toModel() {
        return MemoryDetail.builder()
                .id(this.id)
                .item(this.item)
                .heartRate(this.heartRate)
                .pace(this.pace)
                .kcal(this.kcal)
                .build();
    }

    public MemoryDetailEntity update(MemoryDetailEntity mde) {
        this.item = mde.getItem();
        this.heartRate = mde.getHeartRate();
        this.pace = mde.getPace();
        this.kcal = mde.getKcal();
        return this;
    }
}
