# 택시 배차 api

## 환경

* java : 11
* SpringBoot : 2.5.3
* dababase : mysql
* test : Junit5

## 초기 설정

### docker 설치
* [docker 공식 홈페이지](https://www.docker.com/)

### docker compose 설치
* [docker compose 공식 홈페이지](https://docs.docker.com/compose/install/)

### 어플리케이션 실행
* {Comman line} docker-compose up

## DB Schema

### member

~~~
create table member
(
    id          bigint not null auto_increment,
    created_at  datetime(6) not null,
    updated_at  datetime(6) not null,
    email       varchar(255),
    member_type varchar(255),
    password    varchar(255),
    primary key (id)
) engine=InnoDB;

alter table member
    add constraint UK_email unique (email);
~~~

### taxi_request

~~~
create table taxi_request
(
    id           bigint not null auto_increment,
    created_at   datetime(6) not null,
    updated_at   datetime(6) not null,
    accepted_at  datetime(6),
    address      varchar(100),
    status       varchar(255),
    driver_id    bigint,
    passenger_id bigint not null,
    primary key (id)
) engine=InnoDB;

alter table taxi_request
    add constraint FK_driver_id
        foreign key (driver_id)
            references member (id);
            
alter table taxi_request
    add constraint FK_passenger_id
        foreign key (passenger_id)
            references member (id);
~~~

## API 구성

### 회원가입
* uri : /users/sign-up
* method : POST
* request :
  ~~~
   {
        "email": String,
        "password": String,
        "userType": String
   } 
  ~~~
* response :
  성공
  ~~~
  {
    "id": Long,
    "email": String,
    "userType": String,
    "createdAt": LocalDate,
    "updatedAt": LocalDate
  }
  ~~~
  
  실패1. 이메일 형식이 아닌경우
  ~~~
  {
    "message": "올바른 이메일을 입력해주세요"
  }
  ~~~
  
  실패2. 이미 가입한 메일인 경우
  ~~~
  {
    "message": "이미 가입된 이메일입니다"
  }
  ~~~
  
### 로그인
* uri : /users/sign-in
* method : POST
* request :
  ~~~
   {
        "email": String,
        "password": String
   } 
  ~~~
* response : 
  성공
  ~~~
  {
    "accessToken": String
  }
  ~~~

  실패. 회원 정보가 일치하지 않는 경우
  ~~~
  {
    "message": "아이디와 비밀번호를 확인해주세요"
  }
  ~~~
  
### 모든 배차 요청 목록 조회
* uri : /users/taxi-requests
* method : GET
* header :
  ~~~
   {
        "Authorization": String
   } 
  ~~~
* response :
  성공
  ~~~
  {
    {
    "content": [
        {
            "id": Long,
            "address": String,
            "driverId": Long,
            "passengerId": Long,
            "status": String,
            "acceptedAt": LocalDateTime,
            "createdAt": LocalDateTime,
            "updatedAt": LocalDateTime"
        }
    ],
    "pageable": {
        "sort": {
            "sorted": boolean,
            "unsorted": boolean,
            "empty": boolean
        },
        "pageNumber": int,
        "pageSize": int,
        "offset": int,
        "paged": boolean,
        "unpaged": boolean
    },
    "totalPages": int,
    "totalElements": long,
    "last": boolean,
    "first": boolean,
    "sort": {
        "sorted": boolean,
        "unsorted": boolean,
        "empty": boolean
    },
    "numberOfElements": int,
    "size": int,
    "number": int,
    "empty": boolean
    }
  }
  ~~~
  
  실패. 인증 정보가 유효하지 않은 경우
  ~~~
  {
    "message": "로그인이 필요합니다"
  }
  ~~~

  

### 승객 - 배차 요청
* uri : /users/taxi-requests
* method : POST
* header :
  ~~~
   {
        "Authorization": String
   } 
  ~~~
* request :
  ~~~
   {
      "address": String
   } 
  ~~~
* response :
  성공
  ~~~
  {
      "id": Long,
      "address": String,
      "driverId": Long,
      "passengerId": Long,
      "status": String,
      "acceptedAt": LocalDateTime,
      "createdAt": LocalDateTime,
      "updatedAt": LocalDateTime" 
  }
  ~~~

  실패1. 입력한 주소의 길이가 100 이상인 경우
  ~~~
  {
    "message": "주소는 100자 이하로 입력해주세요"
  }
  ~~~

  실패2. 입력한 주소가 없는 경우
  ~~~
  {
    "message": "주소를 입력해주세요"
  }
  ~~~

  실패3. 인증 정보가 유효하지 않은 경우
  ~~~
  {
    "message": "로그인이 필요합니다"
  }
  ~~~

  실패4. 기사가 요청한 경우
  ~~~
  {
    "message": "승객만 배차 요청할 수 있습니다"
  }
  ~~~

  실패5. 이미 요청한 배차가 있는 경우
  ~~~
  {
    "message": "아직 대기중인 배차 요청이 있습니다"
  }
  ~~~

### 기사 - 배차 요청 수락
* uri : /users/sign-in
* method : POST
* header :
  ~~~
   {
        "Authorization": String
   } 
  ~~~
* path variable :
  ~~~
   {
        "taxiRequestId": Long
   } 
  ~~~
* response :
  성공
  ~~~
  {
      "id": Long,
      "address": String,
      "driverId": Long,
      "passengerId": Long,
      "status": String,
      "acceptedAt": LocalDateTime,
      "createdAt": LocalDateTime,
      "updatedAt": LocalDateTime"
  }
  ~~~

  실패1. 인증 정보가 유효하지 않은 경우
  ~~~
  {
    "message": "로그인이 필요합니다"
  }
  ~~~

  실패2. 승객이 요청한 경우
  ~~~
  {
    "message": "기사만 배차 요청을 수락할 수 있습니다"
  }
  ~~~

  실패3. 요청이 없는 경우
  ~~~
  {
    "message": "존재하지 않는 배차 요청입니다"
  }
  ~~~

  실패4. 이미 수락된 요청인 경우
  ~~~
  {
    "message": "수락할 수 없는 배차 요청입니다. 다른 배차 요청을 선택하세요"
  }
  ~~~

