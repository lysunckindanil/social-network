package org.example.webservice.util;

import lombok.RequiredArgsConstructor;
import org.example.webservice.model.Profile;
import org.example.webservice.repo.ProfileRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@RequiredArgsConstructor
public class ProfileValidator implements Validator {

    private final ProfileRepository profileRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Profile.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Profile profile = (Profile) target;
        String username = profile.getUsername();
        if (profileRepository.findByUsername(username).isPresent())
            errors.rejectValue("username", "", "Profile with this username already exists");
    }
}
