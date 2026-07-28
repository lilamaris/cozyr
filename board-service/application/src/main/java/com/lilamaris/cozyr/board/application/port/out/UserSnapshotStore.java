package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.domain.UserSnapshot;

public interface UserSnapshotStore {
    UserSnapshot upsert(UserSnapshot userSnapshot);
}
