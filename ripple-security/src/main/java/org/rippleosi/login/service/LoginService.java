package org.rippleosi.login.service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.oidc.profile.OidcProfile;
import org.rippleosi.users.model.UserPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    public void saveUserProfile(final WebContext context) {
        String rawIDToken = context.getRequestParameter("id_token");
        Jwt idToken = JwtHelper.decode(rawIDToken);

        try {
            String idClaims = idToken.getClaims();

            JWTClaimsSet idClaimsSet = JWTClaimsSet.parse(idClaims);

            Map<String, Object> claims = idClaimsSet.getClaims();

            Date expiry = (Date) claims.get("exp");

            OidcProfile profile = generateOidcProfile(rawIDToken, expiry.getTime());
            profile.setIdTokenString(rawIDToken);
            profile.addAttributes(claims);

            loadUserMetaData(idClaimsSet, claims, profile);
            loadAppMetadata(idClaimsSet, claims, profile);

            saveToProfileManager(context, profile);
        }
        catch (ParseException pe) {
            LOGGER.error("Could not parse id claims raw json", pe);
        }
    }

    private void loadUserMetaData(JWTClaimsSet idClaimsSet, Map<String, Object> claims, OidcProfile profile) {
        Map<String, String> userMetadata = (Map<String, String>) claims.get("user_metadata");

        if (profile.getAttribute("given_name") == null) {
            profile.addAttribute("given_name", userMetadata.get("given_name"));
        }

        if (profile.getFamilyName() == null) {
            profile.addAttribute("family_name", userMetadata.get("family_name"));
        }
    }

    private void loadAppMetadata(JWTClaimsSet idClaimsSet, Map<String, Object> claims, OidcProfile profile) {
        Map<String, String> appMetadata = (Map<String, String>) claims.get("app_metadata");

        String rawRoles = appMetadata.get("role");
        String[] roles;

        if (rawRoles.contains(",")) {
            roles = StringUtils.split(rawRoles, ",");
        }
        else {
            roles = new String[]{ rawRoles };
        }

        for (String role: roles) {
            profile.addRole(role);
        }

        profile.addAttribute("tenant", appMetadata.get("tenant"));
        profile.addAttribute("nhs_number", appMetadata.get("nhs_number"));

        UserPermissions userPermissions = new UserPermissions(roles);
        profile.addPermissions(userPermissions.loadUserPermissions());
    }

    private OidcProfile generateOidcProfile(String rawIdToken, Long expiry) {
        BearerAccessToken bearerAccessToken = new BearerAccessToken(rawIdToken, expiry, Scope.parse(""));

        return new OidcProfile(bearerAccessToken);
    }

    private void saveToProfileManager(WebContext context, UserProfile profile) {
        LOGGER.debug("profile: {}", profile);

        if (profile != null) {
            ProfileManager manager = new ProfileManager(context);

            manager.save(true, profile);
        }
    }
}
