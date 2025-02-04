package com.eighttoten.support;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {
    private final CustomAuditorAware<String> customAuditorAware;

    @Override
    public Optional<String> getCurrentAuditor() {
        return customAuditorAware.getCurrentAuditor();
    }
}
