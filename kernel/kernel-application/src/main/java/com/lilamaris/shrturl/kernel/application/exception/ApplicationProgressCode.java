package com.lilamaris.shrturl.kernel.application.exception;

public interface ApplicationProgressCode extends ApplicationCode {
    ProcessReason reason();

    String resourceName();
}
