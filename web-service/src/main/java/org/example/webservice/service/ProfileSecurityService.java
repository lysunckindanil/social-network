package org.example.webservice.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.webservice.model.Profile;
import org.example.webservice.model.Role;
import org.example.webservice.repo.ProfileRepository;
import org.example.webservice.repo.RoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileSecurityService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.findByName("ROLE_USER") == null) {
            roleRepository.save(new Role("ROLE_USER"));
        }
    }

    public void createProfile(Profile profile) {
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        profile.setRoles(List.of(roleRepository.findByName("ROLE_USER")));
        profileRepository.save(profile);
    }

    // related to spring security
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Profile> user_optional = profileRepository.findByUsername(username);
        if (user_optional.isEmpty())
            throw new UsernameNotFoundException(username);
        Profile user = user_optional.get();
        return new User(user.getUsername(), user.getPassword(), getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }
}
