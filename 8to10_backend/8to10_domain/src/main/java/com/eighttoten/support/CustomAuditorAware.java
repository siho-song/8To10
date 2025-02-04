package com.eighttoten.support;

import java.util.Optional;

public interface CustomAuditorAware<T> {
    Optional<T> getCurrentAuditor();
}
