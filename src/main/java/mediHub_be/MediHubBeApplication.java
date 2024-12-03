package mediHub_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// DB 없이 swagger test 해보려고 주석처리함. db 설정 후 주석 삭제하기
// @EnableJpaAuditing
// 테스트 위해 JPA 자동 설정 비활성화 - 나중에 괄호 없애기
@SpringBootApplication
public class MediHubBeApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediHubBeApplication.class, args);
    }
}
