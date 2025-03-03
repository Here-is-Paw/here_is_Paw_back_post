package com.ll.hereispaw.global.initData;

import com.ll.hereispaw.domain.member.member.entity.Member;
import com.ll.hereispaw.domain.member.member.service.MemberService;
import com.ll.hereispaw.domain.missing.Auhtor.entity.Author;
import com.ll.hereispaw.domain.missing.missing.dto.request.MissingRequestDTO;
import com.ll.hereispaw.domain.missing.missing.service.MissingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Profile("!prod")
@Configuration
@RequiredArgsConstructor
@Slf4j
public class InitData {
    private final MemberService memberService;
    private final MissingService missingService;

    @Bean
    @Order(3)
    public ApplicationRunner initDataNotProd() {
        return new ApplicationRunner() {

            @Transactional
            @Override
            public void run (ApplicationArguments args) {
                if (memberService.count() > 0)  return;
                Member member1 = memberService.signup("user1", "1234", "유저1", "");
                Member member2 = memberService.signup("user2", "1234", "유저2", "");
                Member member3 = memberService.signup("user3", "1234", "유저3", "");

                Author author1 = missingService.of(member1);
                Author author2 = missingService.of(member2);
                Author author3 = missingService.of(member3);

                // 서울 강남역 좌표 (중심점)
                double centerLat = 37.498095;
                double centerLng = 127.027610;

                // 주변 위치들 (1km 내외 분포)
                double[][] locations = {
                        // 강남역 (중심점)
                        {centerLat, centerLng}, //"서울 강남구 강남역"

                        // 300m 이내 위치들
                        {centerLat + 0.001, centerLng + 0.001 }, // 약 150m "서울 강남구 신논현역 방향"
                        {centerLat - 0.001, centerLng - 0.001 }, // 약 150m "서울 강남구 역삼역 방향"
                        {centerLat + 0.002, centerLng },         // 약 220m "서울 강남구 논현동 방향"

                        // 500m 이내 위치들
                        {centerLat, centerLng + 0.004 },         // 약 440m "서울 강남구 신사동 방향"
                        {centerLat - 0.004, centerLng },         // 약 440m "서울 강남구 역삼동 방향"

                        // 1km 이내 위치들
                        {centerLat + 0.006, centerLng + 0.006 }, // 약 950m "서울 강남구 압구정동 방향"
                        {centerLat - 0.006, centerLng - 0.006 }, // 약 950m "서울 강남구 대치동 방향"

                        // 2km 이상 떨어진 위치들
                        {centerLat + 0.015, centerLng + 0.015 }, // 약 2.3km "서울 강남구 청담동 방향"
                        {centerLat - 0.015, centerLng - 0.015 }  // 약 2.3km "서울 서초구 서초동 방향"
                };

                String[] names = {"초코", "바둑이", "뽀삐", "구름", "토리", "밤비", "라떼", "뭉치", "달이", "콩이"};
                String[] breeds = {"푸들", "말티즈", "포메라니안", "비숑", "닥스훈트", "치와와", "코카스파니엘", "슈나우저", "스피츠", "불독"};
                String[] colors = {"갈색", "흰색", "검정", "회색", "주황색", "베이지", "갈색", "흰색", "검정", "회색"};

                for (int i = 0; i < locations.length; i++) {
                    Author assignedAuthor = (i % 3 == 0) ? author1 : (i % 3 == 1) ? author2 : author3;

                    // PostGIS POINT 형식으로 좌표 저장 (경도, 위도 순서)
                    String geoPoint = "POINT(" + locations[i][1] + " " + locations[i][0] + ")";

                    MissingRequestDTO missingRequest = new MissingRequestDTO(
                            names[i % names.length], // 이름
                            breeds[i % breeds.length], // 견종
                            geoPoint, // PostGIS POINT 형식 좌표
                            "서울 어딘가", // 위치 설명
                            colors[i % colors.length], // 색상
                            "등록번호" + (i + 1), // 등록 번호
                            (i % 2 == 0), // 성별
                            (i % 2 == 0), // 중성화 여부
                            (3 + i), // 나이
                            Timestamp.from(Instant.now().minusSeconds(86400 * i)), // 실종 날짜
                            "특징: 활발함, 위치: " + locations[i][2], // 기타 특징
                            (i % 2 == 0) ? 100000 : 50000, // 사례금
                            1, // 상태
                            assignedAuthor, // 작성자
                            "https://example.com/photo" + (i + 1) // 이미지 URL
                    );

                    missingService.write(missingRequest);
                    log.info("✅ 실종 등록 완료: {} ({} - {})", missingRequest.getName(), missingRequest.getBreed(), locations[i][2]);
                    log.info("📍 좌표: {}", geoPoint);
                }
            }
        };
    }
}