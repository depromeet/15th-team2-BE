package com.depromeet.image.repository;

import com.depromeet.image.Image;
import java.util.List;
import java.util.Optional;

public interface ImageRepository {
    Long save(Image image);

    List<Long> saveAll(List<Image> images);

    Optional<Image> findById(Long id);

    List<Image> findImagesByMemoryId(Long memoryId);

    List<Image> findImageByIds(List<Long> ids);

    void deleteById(Long id);

    void deleteAllByIds(List<Long> ids);

    void deleteAllByMemoryId(Long memoryId);
}