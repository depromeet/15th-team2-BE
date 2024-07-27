package com.depromeet.image.port.in;

import com.depromeet.image.domain.vo.ImagePresignedUrlVo;
import com.depromeet.memory.Memory;
import java.util.List;

public interface ImageUpdateUseCase {
    List<ImagePresignedUrlVo> updateImages(Memory memory, List<String> imageNames);

    void changeImageStatus(List<Long> imageIds);
}
