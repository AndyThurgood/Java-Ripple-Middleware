package org.rippleosi.users.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.pac4j.core.profile.UserProfile;
import org.rippleosi.users.model.UserDetails;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class UserProfileToUserDetailsTransformerTest {

    private UserProfile userProfile;
    private UserProfileToUserDetailsTransformer transformer;

    @Before
    public void setUp() {
        userProfile = new UserProfile();

        userProfile.addAttribute("sub", "1234567890");
        userProfile.addAttribute("username", "test.user");
        userProfile.addAttribute("given_name", "Test");
        userProfile.addAttribute("family_name", "User");
        userProfile.addAttribute("email", "test.user@email.com");
        userProfile.addAttribute("tenant", "Test Tenant");
        userProfile.addAttribute("nhs_number", "1234567890");

        userProfile.addRole("PHR");

        transformer = new UserProfileToUserDetailsTransformer();
    }

    @Test
    public void mustReturnUserDetailsObject() {
        final UserDetails userDetails = transformer.transform(userProfile);

        assertNotNull("UserDetails object must not be null", userDetails);
    }

    @Test
    public void mustReturnValidUserDetailsObjectWithValidInputs() {
        final UserDetails userDetails = transformer.transform(userProfile);

        assertEquals("UserDetails 'sub' field was not set.", userProfile.getAttribute("sub"), userDetails.getSub());

        assertEquals("UserDetails 'preferred_username' field was not set.", userProfile.getAttribute("username"), userDetails.getUsername());

        assertEquals("UserDetails 'given_name' field was not set.", userProfile.getAttribute("given_name"), userDetails.getGivenName());

        assertEquals("UserDetails 'family_name' field was not set.", userProfile.getAttribute("family_name"), userDetails.getFamilyName());

        assertEquals("UserDetails 'email' field was not set.", userProfile.getAttribute("email"), userDetails.getEmail());

        assertEquals("UserDetails 'tenant' field was not set.", userProfile.getAttribute("tenant"), userDetails.getTenant());

        assertEquals("UserDetails 'nhs_number' field was not set.", userProfile.getAttribute("nhs_number"), userDetails.getNhsNumber());

        Object[] roles = userProfile.getRoles().toArray();
        assertEquals("User details 'role' field was not set", roles[0], userDetails.getRole());
        assertArrayEquals("UserDetails 'roles' field was not set.", roles, userDetails.getRoles());

        List<String> permissions = userDetails.getPermissions();
        assertNotNull("UserDetails 'permissions' field was not set.", permissions);
        assertFalse("UserDetails 'permissions' field is empty.", permissions.isEmpty());
    }
}
