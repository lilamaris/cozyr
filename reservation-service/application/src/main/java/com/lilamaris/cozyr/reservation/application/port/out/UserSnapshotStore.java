package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.UserSnapshot;

public interface UserSnapshotStore {
    UserSnapshot upsert(UserSnapshot userSnapshot);
}
