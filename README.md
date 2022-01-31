
## 핵심 문제해결 전략

### 인증 기능
- 대용량 트래픽에 대비해서 무상태를 유지할 수 있는 JWT 토큰방식 인증을 선택했습니다.
- SpringSecurity의 필터체인을 커스텀하는 방식으로 토큰방식 인증을 적용했습니다.

### 파일 업로드
- `MultipartFile` 타입으로 업로도 된 파일을 `AWS S3`에 업로드 하는 방식으로 구현했습니다.
- DB에는 원본파일의 이름과 S3에 업로드 된 URL이 저장되도록 구성햇습니다.

### 파일 삭제
- 파일 삭제의 경우 삭제 요청시 바로 삭제하는 방식이 아닌 체크 후 `스케줄러`를 이용해서 나중에 일괄삭제하는 `Soft Delete` 방식을 사용했습니다.
- 파일 삭제 요청이 들어올 때마다 DB에서 지우고 S3에 업로드 된 파일을 지우는 것은 성능상 손해라고 판단했습니다.

### 쿼리 성능 최적화
- `ToOne` 관계의 경우 모두 `Lazy`로 설정하여 프록시 객체를 이용해서 사용하지 않는 데이터를 위한 쿼리를 줄였고, 이로 인해 발생할 수 있는 `N+1` 문제는 `fetch join`과 `Dto`로 직접조회하는 방식으로 해결했습니다.

### 테스트
- 주요 기능에 대한 단위 테스트와 API 테스트 코드를 작성했습니다.

***

## 실행방법

- S3 bucket 생성 후 `PutObject`, `DeleteObject` 권한을 설정한다.
- 환경변수를 등록한다.

> AWS_ACCESS_KEY=[AWS엑세스_키];AWS_SECRET_KEY=[AWS시크릿_키];S3_BUCKET_NAME=[S3_버킷_이름]

- `application.yml`에 `datasource`정보를 설정한다.

***

## API 문서

![image](https://user-images.githubusercontent.com/77182648/151708545-a37861ac-e53b-4303-ad56-1c589379b3e3.png)

***

## 사용기술
- Springboot 2.6.3
- ORM: Spring Data Jpa, QueryDSL
- 인증: SpringSecurity, JWT
- 파일저장: AWS S3
- 데이터베이스: H2

