package com.eighttoten.support;

import com.eighttoten.member.domain.Member;

public interface AuthAccessor {
    Member getAuthenticatedMember();
}
