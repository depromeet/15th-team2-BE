package com.depromeet.pool.api;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.pool.dto.request.FavoritePoolCreateRequest;
import com.depromeet.pool.dto.response.PoolInitialResponse;
import com.depromeet.pool.dto.response.PoolSearchResponse;
import com.depromeet.pool.service.PoolService;
import com.depromeet.security.LoginMember;
import com.depromeet.type.pool.PoolSuccessType;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pool")
public class PoolController implements PoolApi {
    private final PoolService poolService;

    @GetMapping("/search")
    public ApiResponse<PoolSearchResponse> searchPoolsByNameQuery(
            @RequestParam(value = "nameQuery") String nameQuery) {
        return ApiResponse.success(
                PoolSuccessType.SEARCH_SUCCESS, poolService.findPoolsByName(nameQuery));
    }

    @GetMapping("/search/initial")
    public ApiResponse<PoolInitialResponse> getFavoriteAndSearchedPools(
            @LoginMember Long memberId) {
        return ApiResponse.success(
                PoolSuccessType.INITIAL_GET_SUCCESS,
                poolService.getFavoriteAndSearchedPools(memberId));
    }

    @PostMapping("/favorite")
    public ResponseEntity<URI> createFavoritePool(
            @LoginMember Long memberId, @Valid @RequestBody FavoritePoolCreateRequest request) {
        String uri = poolService.createFavoritePool(memberId, request);
        return ResponseEntity.created(URI.create(uri)).build();
    }
}
