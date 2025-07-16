package br.ufscar.dc.dsw.backend.service;

import br.ufscar.dc.dsw.backend.domain.User;
import br.ufscar.dc.dsw.backend.dto.UserResponseDTO;
import br.ufscar.dc.dsw.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDTO> getPotentialFriends(Long currentUserId){
        List<User> users = userRepository.findPotentialFriends(currentUserId);
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    };

    private UserResponseDTO convertToDto(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername());
    }

}
