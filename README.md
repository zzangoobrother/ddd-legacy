# 키친포스

## 퀵 스타트

```sh
cd docker
docker compose -p kitchenpos up -d
```

## 요구 사항

### 상품
#### 등록
- [ ] 상품 등록은 이름과 가격을 반드시 입력한다.
- [ ] 상품 이름은 공백이나 빈값을 등록할 수 없다.
- [ ] 상품 이름은 비속어를 포함 할 수 없다.
- [ ] 상품 가격은 0원 보다 작을 수 없다.

#### 상품 가격 수정
- [ ] 상품 가격을 반드시 입력한다.
- [ ] 상품 가격은 0원 보다 작을 수 없다.
- [ ] 상품 가격 변경으로 메뉴-상품 가격 총합이 메뉴 가격보다 작으면 메뉴 노출 여부를 숨김으로 변경한다.

#### 상품 목록 조회
- [ ] 상품의 전체 내용을 조회할 수 있다.

### 메뉴 그룹
#### 등록
- [ ] 메뉴 그룹 이름은 반드시 입력한다.
- [ ] 메뉴 그룹 이름은 공백이나 빈값을 등록할 수 없다.

#### 메뉴 그룹 목록 조회
- [ ] 메뉴 그룹 전체 내용을 조회할 수 있다.

### 메뉴
#### 등록
- [ ] 메뉴는 반드시 가격을 입력한다.
    - [ ] 메뉴 가격은 0원 보다 작거나 빈값일 수 없다.
- [ ] 메뉴는 메뉴 그룹에 속해야 한다.
- [ ] 메뉴는 반드시 하나 이상의 메뉴 상품을 가진다.
    - [ ] 메뉴 상품의 수량은 0보다 커야한다.
    - [ ] 메뉴의 가격은 메뉴 상품 가격의 총합보다 클 수 없다.
- [ ] 메뉴 이릅은 반드시 입력한다.
- [ ] 메뉴 이름에는 비속어를 포함할 수 없다.

#### 가격 수정
- [ ] 메뉴는 반드시 가격을 입력한다.
- [ ] 메뉴 가격은 0원 보다 작거나 빈값일 수 없다.
- [ ] 메뉴 가격이 메뉴 상품의 총합보다 클 수 없다.

#### 메뉴 노출로 변경
- [ ] 메뉴 노출 여부 노출로 변경한다.
- [ ] 메뉴 가격이 메뉴 상품 가격의 총합보다 클 수 없다.

#### 메뉴 숨김 견경
- [ ] 메뉴 노출 여부 숨김으로 변경한다.

#### 메뉴 목록 조회
- [ ] 메뉴 전체를 조회할 수 있다.
- [ ] 조회 내용에는 메뉴에 포함되는 상품 내용도 포함된다.

### 주문 테이블
#### 등록
- [ ] 주문 테이블 이름을 반드시 입력한다.
- [ ] 주문 테이블 이름은 공백이나 빈값을 등록할 수 없다.
- [ ] 주문 테이블 등록시 빈 테이블로 등록한다..

#### 착석으로 변경
- [ ] 주문 테이블에 착석으로 변경한다.

#### 빈 테이블로 변경
- [ ] 주문 테이블의 주문 상태가 완료 상태인지 확인한다.
- [ ] 주문 테이블을 빈 테이블 상태로 세팅한다.

#### 손님 수 변경
- [ ] 입력 받은 손님 수는 반드시 0보다 큽니다.
- [ ] 착석 중인 테이블만 손님 수를 변경할 수 있습니다.

#### 주문 테이블 목록 조회
- [ ] 주문 테이블 전체 내용을 조회 한다.

### 주문
#### 등록
- 배달
- [ ] 주문 타입은 반드시 입력한다.
  - [ ] 주문 타입은 공백을 등록할 수 없다.
- [ ] 주문 아이템은 반드시 메뉴를 입력한다.
- [ ] 주문 아이템 수량은 1개 이상 이어야 한다.
- [ ] 주문 아이템에 노출되지 않은 메뉴가 포함되지 않아야 한다.
- [ ] 주문 아이템 가격과 메뉴의 가격은 같아야 한다.
- [ ] 주문시 처음 주문 상태는 대기 상태이다.
- [ ] 반드시 배달 주소를 입력받는다.
- [ ] 배달 주소는 공백이나 빈값을 입력받을 수 없다.


- 매장 식사
- [ ] 주문 타입은 반드시 입력한다.
  - [ ] 주문 타입은 공백을 등록할 수 없다.
- [ ] 주문 아이템은 반드시 메뉴를 입력한다.
- [ ] 주문 아이템에 노출되지 않은 메뉴가 포함되지 않아야 한다.
- [ ] 주문 아이템 가격과 메뉴의 가격은 같아야 한다.
- [ ] 주문시 처음 주문 상태는 대기 상태이다.
- [ ] 반드시 주문 테이블을 갖는다.
- [ ] 주문 테이블의 상태는 빈 테이블이어야 한다.


- 포장
- [ ] 주문 타입은 반드시 입력한다.
  - [ ] 주문 타입은 공백을 등록할 수 없다.
- [ ] 주문 아이템은 반드시 메뉴를 입력한다.
- [ ] 주문 아이템 수량은 1개 이상 이어야 한다.
- [ ] 주문 아이템에 노출되지 않은 메뉴가 포함되지 않아야 한다.
- [ ] 주문 아이템 가격과 메뉴의 가격은 같아야 한다.
- [ ] 주문시 처음 주문 상태는 대기 상태이다.

#### 주문 수락
- [ ] 주문 수락시 주문 상태는 대기 상태여야 한다.
- [ ] 주문 타입이 배달이라면
  - [ ] 배달 클라이언트에게 배달을 요청한다.

#### 서빙
- [ ] 주문 상태가 수락 상태이면 서빙을 한다.
- [ ] 주문 상태를 서빙 상태로 변경한다.

#### 배달 시작
- [ ] 주문 타입이 배달이어야 한다.
- [ ] 주문 상태가 서빙 상태이어야 한다.
- [ ] 주문 상태를 배달 중으로 변경한다.

#### 배달 완료
- [ ] 주문 상태가 배달 중이어야 한다.
- [ ] 주문 상태를 배달 완료로 변경한다.

#### 완료
- [ ] 주문 타입이 배달이면
  - [ ] 주문 상태는 배달 완료 상태여야 한다.
- [ ] 주문 타입이 테이크 아웃 또는 매장 내 식사 이면
  - [ ] 주문 상태는 서빙 완료 상태여야 한다.
- [ ] 주문 상태를 완료 상태로 변경한다.
- [ ] 주문 상태가 매장 내 식사인 경우
  - [ ] 해당 주문 테이블을 손님 수 0명, 착석 여부 비어있는 상태로 세팅한다.

#### 주문 목록 조회
- [ ] 주문 전체를 조회 한다.
- [ ] 조회 내용에는 메뉴와 주문 테이블 내용도 포함된다.


#### 

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
|  |  |  |

## 모델링
