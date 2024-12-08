package mediHub_be.admin.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminCreateDTO;
import mediHub_be.part.entity.Part;
import mediHub_be.part.repository.PartRepository;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.ranking.repository.RankingRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final RankingRepository rankingRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(AdminCreateDTO adminDTO) {
        // 중복 사용자 확인
        if (userRepository.findByUserId(adminDTO.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다: " + adminDTO.getUserId());
        }

        Part part = partRepository.findById(adminDTO.getPartSeq())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서 ID입니다."));
        Ranking ranking = rankingRepository.findById(adminDTO.getRankingSeq())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 등급 ID입니다."));

        User user = new User(
                adminDTO.getUserId(),
                passwordEncoder.encode(adminDTO.getUserPassword()),
                adminDTO.getUserName(),
                adminDTO.getUserEmail(),
                adminDTO.getUserPhone(),
                part,
                ranking
        );

        return userRepository.save(user);
    }
}
