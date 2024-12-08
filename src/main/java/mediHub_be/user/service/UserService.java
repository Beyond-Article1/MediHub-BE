package mediHub_be.user.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.part.entity.Part;
import mediHub_be.part.repository.PartRepository;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.ranking.repository.RankingRepository;
import mediHub_be.user.dto.UserDTO;
import mediHub_be.user.dto.UserUpdateDTO;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final RankingRepository rankingRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setUserSeq(user.getUserSeq());
                    dto.setUserId(user.getUserId());
                    dto.setUserName(user.getUserName());
                    dto.setUserEmail(user.getUserEmail());
                    dto.setUserPhone(user.getUserPhone());
                    dto.setPartSeq(user.getPart().getPartSeq());
                    dto.setRankingSeq(user.getRanking().getRankingSeq());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 특정 회원 조회
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        UserDTO dto = new UserDTO();
        dto.setUserSeq(user.getUserSeq());
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserPhone(user.getUserPhone());
        dto.setPartSeq(user.getPart().getPartSeq());
        dto.setRankingSeq(user.getRanking().getRankingSeq());
        return dto;
    }

    // 자기 회원 정보 수정
    @Transactional
    public void updateUser(Long userSeq, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Part part = partRepository.findById(updateDTO.getPartSeq())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서 ID입니다."));
        Ranking ranking = rankingRepository.findById(updateDTO.getRankingSeq())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 등급 ID입니다."));

        user.updateUser(
                updateDTO.getUserPassword() != null ? passwordEncoder.encode(updateDTO.getUserPassword()) : user.getUserPassword(),
                updateDTO.getUserName(),
                updateDTO.getUserEmail(),
                updateDTO.getUserPhone(),
                part,
                ranking
        );

        userRepository.save(user);
    }
}
