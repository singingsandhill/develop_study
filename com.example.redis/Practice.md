

-- INCR articles:{id}
INCR articles:1
INCR articles:2

-- 오늘의 조회수 따로 관리
INCR articles:1:today
RENAME articles:1:today articles:2025-03-07

flushdb

-- 계정당 조회 제한
SADD articles:1 alex
SADD articles:1 brad
SADD articles:1 chad
scard articles:1

sadd articles:2 alex
sadd  articles:2 chad

scard articles:2

ZADD articles:ranks 1 articles:1

-- ZINCRBY 명령어는 **Sorted Set(정렬된 집합)**의 특정 멤버의 점수를 증가시키는 역할
ZINCRBY  articles:ranks 1 articles:1

-- 가장 높은 조회수 글 조회
ZREVRANGE articles:ranks 0 0
zrange articles:ranks 0 0 rev

-- ==================================================================================================

-- 1. 주문 ID, 판매 물품, 갯수, 총액, 결제 여부에 대한 데이터를 지정하기 위한 `ItemOrder` 클래스를 `RedisHash`로 만들고,
--     1. 주문 ID - String
--     2. 판매 물품 - String
--     3. 갯수 - Integer
--     4. 총액 - Long
--     5. 주문 상태 - String
-- 2. 주문에 대한 CRUD를 진행하는 기능을 만들어보자.
--     1. `ItemOrder`의 속성값들을 ID를 제외하고 클라이언트에서 전달해준다.
--     2. 성공하면 저장된 `ItemOrder`를 사용자에게 응답해준다.
