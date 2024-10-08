package com.depromeet.fixture.domain.pool;

import com.depromeet.pool.domain.Pool;

public class PoolFixture {
    public static Pool make(String name, String address, Integer lane) {
        return Pool.builder().name(name).address(address).lane(lane).build();
    }
}
