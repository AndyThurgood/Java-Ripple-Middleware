package org.rippleosi.logout.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.oidc.profile.OidcProfile;
import org.rippleosi.common.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @Value("${authentication.server.url}")
    protected String authServerUrl;

    @Value("${authentication.client.id}")
    private String clientId;

    @Value("${pac4j.applicationLogout.logoutUrl}")
    protected String logoutUrl;

    @Value("${pac4j.callback.defaultUrl}")
    private String defaultUrl;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<String> logout(final HttpServletRequest request, final HttpServletResponse response) {
        WebContext context = new J2EContext(request, response);

        ProfileManager<OidcProfile> manager = new ProfileManager<>(context);
        manager.logout();

        String fullLogoutUrl = authServerUrl + logoutUrl;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("returnTo", defaultUrl);
        queryParams.put("client_id", clientId);

        return securityService.generateRedirectResponseEntity(fullLogoutUrl, queryParams, HttpStatus.OK);
    }
}
