package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;

public interface RoomPolicyStore {
    RoomOpPolicy save(RoomOpPolicy policy);
}
