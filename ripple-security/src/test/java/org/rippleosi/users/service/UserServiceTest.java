package org.rippleosi.users.service;

import org.junit.Before;
import org.junit.Test;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.profile.UserProfile;
import org.rippleosi.users.model.UserDetails;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.assertNotNull;

public class UserServiceTest {

    private final String ID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InJpcHBsZW9zaS5jbGluaWNpY" +
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

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private UserProfile userProfile;
    private UserService userService;

    @Before
    public void setUp() {
        request = new MockHttpServletRequest("GET", "/test");
        request.setParameter("id_token", ID_TOKEN);
        request.setParameter("scope", "test");
        request.setParameter("expires_in", "3600");

        response = new MockHttpServletResponse();

        userProfile = new UserProfile();
        userProfile.addAttribute("sub", "1234567890");
        userProfile.addAttribute("preferred_username", "test.user");
        userProfile.addAttribute("given_name", "Test");
        userProfile.addAttribute("family_name", "User");
        userProfile.addAttribute("email", "test.user@email.com");
        userProfile.addAttribute("tenant", "Test Tenant");
        userProfile.addAttribute("nhs_number", "1234567890");
        userProfile.addRole("TEST_ROLE");

        userService = new UserService();
    }

    @Test
    public void mustBeAbleToRetrieveActiveUserProfileForTheSession() {
        request.setAttribute(Pac4jConstants.USER_PROFILE, userProfile);

        final UserDetails userDetails = userService.findUserDetails(request, response);

        assertNotNull("Active UserProfile could not be found.", userDetails);
    }
}
