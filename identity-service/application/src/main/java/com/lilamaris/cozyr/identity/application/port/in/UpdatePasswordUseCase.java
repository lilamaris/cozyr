package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.port.in.command.UpdatePasswordCommand;

public interface UpdatePasswordUseCase {
    void update(UpdatePasswordCommand command);
}
