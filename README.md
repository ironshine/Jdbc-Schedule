### API 명세서

| 기능           | Method     | URL | Request| Response |
|----------------|----------|-------------|------|------|
| 일정 작성 | `POST` | /api/schedule | 요청 body | 작성정보 |
| 일정 전체조회 | `GET` | /api/schedule | - | 일정전체 정보 |
| 선택한 일정 조회 | `GET` | /api/schedule/{id} | 요청 query | 선택한 일정 정보 |
| 일정 목록 조회 | `GET` | /api/schedule | 요청 param | 일정 목록 정보 |
| 일정 수정 | `PUT` | /api/schedule/update | 요청 body | 일정 수정 정보 |
| 일정 삭제 | `DELETE` | /api/schedule/delete | 요청 body | - |
