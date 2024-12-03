package mediHub_be.cp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CpService {

    private final CpRepository cpRepository;
    private final CpVersionRepository cpVersionRepository;
}
