package com.depromeet.memory.domain;

import com.depromeet.image.domain.Image;
import com.depromeet.member.domain.Member;
import com.depromeet.pool.domain.Pool;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Memory {
    private Long id;
    private Member member;
    private Pool pool;
    private MemoryDetail memoryDetail;
    private List<Stroke> strokes;
    private List<Image> images;
    private LocalDate recordAt;
    private LocalTime startTime;
    private LocalTime endTime;
    private Short lane;
    private String diary;
    private LocalDateTime updatedAt;

    @Builder
    public Memory(
            Long id,
            Member member,
            Pool pool,
            MemoryDetail memoryDetail,
            List<Stroke> strokes,
            List<Image> images,
            LocalDate recordAt,
            LocalTime startTime,
            LocalTime endTime,
            Short lane,
            String diary,
            LocalDateTime updatedAt) {
        this.id = id;
        this.member = member;
        this.pool = pool;
        this.memoryDetail = memoryDetail;
        this.strokes = strokes;
        this.images = images;
        this.recordAt = recordAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lane = lane;
        this.diary = diary;
        this.updatedAt = updatedAt;
    }

    public void setStrokes(List<Stroke> strokes) {
        if (strokes != null && !strokes.isEmpty()) {
            this.strokes = strokes;
        }
    }

    public Memory update(Memory updateMemory) {
        return Memory.builder()
                .id(id)
                .member(member)
                .pool(updateMemory.getPool())
                .memoryDetail(updateMemory.getMemoryDetail())
                .strokes(updateMemory.getStrokes())
                .images(images)
                .recordAt(updateMemory.getRecordAt())
                .startTime(updateMemory.getStartTime())
                .endTime(updateMemory.getEndTime())
                .lane(updateMemory.getLane())
                .diary(updateMemory.getDiary())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public String parseStartTime() {
        if (this.startTime == null) {
            return null;
        }
        return this.startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String parseEndTime() {
        if (this.endTime == null) {
            return null;
        }
        return this.endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String classifyType() {
        if (this.strokes == null || this.strokes.isEmpty()) {
            return "NORMAL";
        } else if (this.strokes.size() == 1) {
            return "SINGLE";
        } else {
            return "MULTI";
        }
    }

    public Integer calculateTotalDistance() {
        if (this.strokes == null || this.strokes.isEmpty()) return null;

        int totalDistance = 0;
        for (Stroke stroke : this.strokes) {
            int meter = stroke.getMeter() != null ? stroke.getMeter() : 0;
            float laps = stroke.getLaps() != null ? stroke.getLaps() : 0;

            if (meter != 0) {
                totalDistance += meter;
            } else {
                if (this.lane != null) {
                    totalDistance += (int) (laps * 2) * this.lane;
                }
            }
        }
        return totalDistance;
    }

    public String getThumbnailUrl() {
        if (this.images == null || this.images.isEmpty()) return null;

        Image image = this.images.getFirst();
        return image.getImageUrl();
    }

    public boolean isAchieved(Integer totalDistance) {
        if (totalDistance == null) return false;
        return totalDistance >= this.member.getGoal();
    }

    public Memory setMemoryDetailNull() {
        this.memoryDetail = null;
        return this;
    }

    public int getLaneFromMemoryOrPool() {
        if (this.lane != null && this.lane != 0) {
            return this.lane;
        }
        if (this.pool != null) {
            return this.pool.getLane() != null ? this.pool.getLane() : 0;
        }
        return 0;
    }
}
