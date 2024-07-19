package com.depromeet.dto.response;

import lombok.Builder;

public record CustomSliceResponse<T>(
        T content, int pageNumber, int pageSize, boolean hasNext, boolean hasPrevious) {

    @Builder
    public CustomSliceResponse {}
}