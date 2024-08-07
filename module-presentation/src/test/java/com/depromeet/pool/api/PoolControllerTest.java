package com.depromeet.pool.api;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.depromeet.config.ControllerTestConfig;
import com.depromeet.config.mock.WithCustomMockMember;
import com.depromeet.pool.facade.PoolFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(PoolController.class)
public class PoolControllerTest extends ControllerTestConfig {
    @Autowired private ObjectMapper objectMapper;

    @MockBean PoolFacade poolFacade;

    @Test
    @WithCustomMockMember
    void 이름쿼리로_수영장을_조회합니다() throws Exception {
        mockMvc.perform(get("/pool/search").param("nameQuery", "검색인자"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("POOL_1"))
                .andExpect(jsonPath("$.message").value("수영장 검색을 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    void 즐겨찾기_수영장_및_최근_검색_수영장을_조회합니다() throws Exception {
        mockMvc.perform(get("/pool/search/initial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("POOL_2"))
                .andExpect(jsonPath("$.message").value("즐겨찾기 및 최근 검색 수영장 조회를 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    void 수영장을_즐겨찾기에_등록합니다() throws Exception {
        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("poolId", 1L);
        BDDMockito.given(poolFacade.putFavoritePool(any(), any())).willReturn("1");

        mockMvc.perform(
                        put("/pool/favorite")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("POOL_3"))
                .andExpect(jsonPath("$.message").value("수영장 즐겨찾기 등록에 성공하였습니다"))
                .andDo(print());
    }

    @Test
    @WithCustomMockMember
    void 수영장을_즐겨찾기에서_삭제합니다() throws Exception {
        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("poolId", 1L);
        BDDMockito.given(poolFacade.putFavoritePool(any(), any())).willReturn(null);

        mockMvc.perform(
                        put("/pool/favorite")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("POOL_4"))
                .andExpect(jsonPath("$.message").value("수영장 즐겨찾기 삭제에 성공하였습니다"))
                .andDo(print());
    }
}
