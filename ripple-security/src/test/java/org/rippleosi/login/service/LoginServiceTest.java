package org.rippleosi.login.service;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.core.profile.UserProfile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LoginServiceTest {

    private static final String ID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InJpcHBsZW9zaS5jbGluaWNpY" +
        "W5AbWFpbC5jb20iLCJ1c2VybmFtZSI6ImNsaW5pY2lhbiIsInVzZXJfbWV0YWRhdGEiOnsiZ2l2ZW5fbmFtZSI6IlRlc3QiLCJmYW1pbHlfbm" +
        "FtZSI6IkNsaW5pY2lhbiJ9LCJuYW1lIjoicmlwcGxlb3NpLmNsaW5pY2lhbkBtYWlsLmNvbSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF" +
        "0YXIuY29tL2F2YXRhci9iNTk0Y2NhYWFhZDAyMDNjZGNjZWQyMTA1YjMzYTliOD9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0" +
        "aDAuY29tJTJGYXZhdGFycyUyRnJpLnBuZyIsIm5pY2tuYW1lIjoiY2xpbmljaWFuIiwiYXBwX21ldGFkYXRhIjp7InJvbGUiOiJJRENSIiwic" +
        "GVybWlzc2lvbnMiOlsiTWFyYW5kIiwiRXRoZXJDSVMiXSwicm9sZVR5cGUiOiJEb2N0b3I6IENsaW5pY2lhbiIsInRlbmFudCI6IlJpcHBsZS" +
        "BDYXJlIFJlY29yZCIsIm5oc19udW1iZXIiOm51bGx9LCJyb2xlIjoiSURDUiIsInBlcm1pc3Npb25zIjpbIk1hcmFuZCIsIkV0aGVyQ0lTIl0" +
        "sInJvbGVUeXBlIjoiRG9jdG9yOiBDbGluaWNpYW4iLCJ0ZW5hbnQiOiJSaXBwbGUgQ2FyZSBSZWNvcmQiLCJlbWFpbF92ZXJpZmllZCI6ZmFs" +
        "c2UsInVwZGF0ZWRfYXQiOiIyMDE2LTEwLTE4VDE0OjU1OjIyLjAwOFoiLCJ1c2VyX2lkIjoiYXV0aDB8NTgwNGU1NzFmNmM3MmE3NDUxZTBlM" +
        "jFkIiwiY3JlYXRlZF9hdCI6IjIwMTYtMTAtMTdUMTQ6NTE6MjkuNjIwWiIsImlzcyI6Imh0dHBzOi8vcmlwcGxlb3NpLmV1LmF1dGgwLmNvbS" +
        "8iLCJzdWIiOiJhdXRoMHw1ODA0ZTU3MWY2YzcyYTc0NTFlMGUyMWQiLCJhdWQiOiJZcUtMVXF2SkZBaVcwcW9mTjZ3TzllWm00V0Q3WnRpRyI" +
        "sImV4cCI6MTQ3NjgwNTUyMiwiaWF0IjoxNDc2ODAyNTIyLCJub25jZSI6Im5vbmNlIn0.PGYRIplx7ZPH4CtQ4yjlBs9p9RnX5MS4_EliX1KbEJw";

    private LoginService loginService;

    @Before
    public void setUp() {
        loginService = new LoginService();
    }

    private J2EContext setUpWebContext() {
        final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        request.setParameter("id_token", ID_TOKEN);
        request.setParameter("scope", "test");
        request.setParameter("expires_in", "3600");

        final MockHttpServletResponse response = new MockHttpServletResponse();

        return new J2EContext(request, response);
    }

    @Test
    public void userProfileMustBeSavedWithValidTokenInputs() {
        final WebContext context = setUpWebContext();

        loginService.saveUserProfile(context);

        final ProfileManager manager = new ProfileManager(context);
        final UserProfile userProfile = manager.get(true);

        assertNotNull("The user profile wasn't saved whilst setting up the security context.", userProfile);
    }

    @Test
    public void userIdAttributesMustBeSavedWithValidTokenInputs() {
        final WebContext context = setUpWebContext();

        loginService.saveUserProfile(context);

        final ProfileManager manager = new ProfileManager(context);
        final UserProfile userProfile = manager.get(true);

        final Map<String, Object> attributes = userProfile.getAttributes();

        assertNotNull("The user's ID attributes weren't saved whilst setting up the security context.", attributes);
        assertTrue("The user's ID attributes weren't saved whilst setting up the security context.", attributes.size() > 0);
    }

    @Test
    public void userRoleMustBeSavedWithValidTokenInputs() {
        final WebContext context = setUpWebContext();

        loginService.saveUserProfile(context);

        final ProfileManager manager = new ProfileManager(context);
        final UserProfile userProfile = manager.get(true);

        final List<String> roles = userProfile.getRoles();

        assertNotNull("The user role wasn't saved whilst setting up the security context.", roles);
        assertEquals("The user role saved doesn't correspond to the input value.", "IDCR", roles.get(0));
    }

    @Test
    public void userProfileMustContainThePermissionsOfTheRole() {
        final WebContext context = setUpWebContext();

        loginService.saveUserProfile(context);

        final ProfileManager manager = new ProfileManager(context);
        final UserProfile userProfile = manager.get(true);

        final List<String> permissions = userProfile.getPermissions();

        assertNotNull("The user's permissions have not been saved in the user profile.", permissions);
    }
}
