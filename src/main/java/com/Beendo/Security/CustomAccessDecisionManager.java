package com.Beendo.Security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

/*@Service*/
public class CustomAccessDecisionManager extends AbstractAccessDecisionManager {
	
	public CustomAccessDecisionManager(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
		super(decisionVoters);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void decide(Authentication authentication, Object filter, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		// TODO Auto-generated method stub
		if ((filter == null) || !this.supports(filter.getClass())) {
			throw new IllegalArgumentException("Object must be a FilterInvocation");
		}

		String url = ((FilterInvocation) filter).getRequestUrl();
		String contexto = ((FilterInvocation) filter).getRequest().getContextPath();

		// Collection<ConfigAttribute> roles =
		// service.getConfigAttributesFromSecuredUris(contexto, url);
		Collection<ConfigAttribute> roles = null;

		int deny = 0;

		for (AccessDecisionVoter voter : getDecisionVoters()) {
			int result = voter.vote(authentication, filter, roles);

			if (logger.isDebugEnabled()) {
				logger.debug("Voter: " + voter + ", returned: " + result);
			}

			switch (result) {
			case AccessDecisionVoter.ACCESS_GRANTED:
				return;

			case AccessDecisionVoter.ACCESS_DENIED:

				deny++;

				break;

			default:
				break;
			}
		}

		if (deny > 0) {
			throw new AccessDeniedException(
					messages.getMessage("AbstractAccessDecisionManager.accessDenied", "Access is denied"));
		}

		// To get this far, every AccessDecisionVoter abstained
		checkAllowIfAllAbstainDecisions();
	}
}
