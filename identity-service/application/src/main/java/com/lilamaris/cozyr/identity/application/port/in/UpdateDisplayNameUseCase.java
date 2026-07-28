package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.port.in.command.UpdateDisplayNameCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.UpdatedDisplayNameResult;

public interface UpdateDisplayNameUseCase {
    UpdatedDisplayNameResult update(UpdateDisplayNameCommand command);
}
