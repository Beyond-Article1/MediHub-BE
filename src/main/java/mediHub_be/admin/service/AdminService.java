package mediHub_be.admin.service;

import lombok.RequiredArgsConstructor;
import mediHub_be.admin.dto.AdminDTO;
import mediHub_be.part.entity.Part;
import mediHub_be.part.repository.PartRepository;
import mediHub_be.ranking.entity.Ranking;
import mediHub_be.ranking.repository.RankingRepository;
import mediHub_be.user.entity.User;
import mediHub_be.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final RankingRepository rankingRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Long registerUser(AdminDTO adminDTO, String adminId) {

        // 관리자 확인
        User admin = userRepository.findByUserId(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자 계정이 필요합니다."));


        // 중복 사용자 확인
        if (userRepository.findByUserId(adminDTO.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다: " + adminDTO.getUserId());
        }

        // 부서 및 등급 조회
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
        userRepository.save(user);

        return user.getUserSeq(); // 생성된 사용자 ID 반환
    }

    @Transactional
    public Long updateUser(Long userSeq, AdminDTO adminDTO) {

        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 부서 및 등급 조회
        Part part = partRepository.findById(adminDTO.getPartSeq())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서 ID입니다."));
        Ranking ranking = rankingRepository.findById(adminDTO.getRankingSeq())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 등급 ID입니다."));

        // 사용자 정보 업데이트
        user.updateUser(
                adminDTO.getUserPassword() != null ? passwordEncoder.encode(adminDTO.getUserPassword()) : user.getUserPassword(),
                adminDTO.getUserName(),
                adminDTO.getUserEmail(),
                adminDTO.getUserPhone(),
                part,
                ranking
        );

        userRepository.save(user);
        return user.getUserSeq();
    }

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<AdminDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    AdminDTO dto = new AdminDTO();
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
    public AdminDTO getUserById(Long userSeq) {
        User user = userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        AdminDTO dto = new AdminDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserPhone(user.getUserPhone());
        dto.setPartSeq(user.getPart().getPartSeq());
        dto.setRankingSeq(user.getRanking().getRankingSeq());
        return dto;
    }
}
