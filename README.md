심화 과제 중 구현사항 (9 of 10)
  -
    1. GlobalExceptionHandler를 통해 CustomException 핸들링하기 (common/exception)

    2. 기존의 Service 클래스를 인터페이스와 구현체로 분리 (*/service)

    3. Controller의 입출력값을 출력하는 logging을 위한 AOP 적용 (common/aop)
     
    4. CustomRepository 인터페이스 생성 후 구현체에서 QueryDSL을 통한 검색 기능 개선 (post/repository)
  
    5. Pageable을 적용하여 원하는 페이지 사이즈만큼 조회 기능 구현 (post/controller)

    6. 사용자 회원가입, 로그인, 단일 회원 조회 기능 확인을 위해 MockMvc를 활용한 Controller 테스트 코드 작성 (user/controller)

    7. 게시글에 태그 등록 및 조회 후 삭제 기능 확인을 위해 @SpringBootTest를 활용한 Service 테스트 코드 작성 (post/service)

    8. AWS S3를 통한 게시글과 프로필 이미지 등록/수정/삭제 기능 구현 (common/service, post/service, profile/service)

    9. AWS EC2를 통한 애플리케이션 .jar 배포하기

기존 과제에 대한 튜터님의 피드백 반영
-
    1. 기존에 좋아요와 팔로우의 등록/해제 기능이 하나로 합쳐져 있던 것을 개방-폐쇄 원칙(OCP)에 따라 분리
    
    2. 최근 3번 이내에 사용된 비밀번호 이력 조회를 @Query로 구현

    3. 비밀번호 수정을 위한 API 요청을 PUT에서 PATCH로 변경
    
    4. Swagger 적용하기
     -> login 기능은 controller가 아닌 filter 단에 구현되었기 때문에 swagger에서 찾을 수 없습니다.
     -> login(@RequestBody LoginRequestDto requestDto)

미흡한 점
-
    1. Mapping 경로의 요소들을 단수형에서 복수형으로 바꾸기 (user -> user's')
    
    2. 심화 과제 중 @DataJpaTest를 활용한 repository 테스트 코드 작성하지 않았습니다.

    3. 백엔드 API 요청들이 대폭 수정 됨에 따라 프론트엔드가 거의 동작하지 않습니다.

이후 시도해볼 점
-
    1. 코드 컨벤션 맞추기
    
    2. RefreshToken

    3. Redis 블랙리스트를 통한 사용자 차단 기능

    4. Spring MVC 활용하기/ restTemplate 활용
