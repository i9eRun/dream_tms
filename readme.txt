저장소 초기화 및 푸시


cd development
git init
git add .
git commit -m "Initial commit: backend + sencha"
git branch -M main
git remote add origin https://github.com/<your-id>/<repo-name>.git
git push -u origin main




다른 pc에서 복원


git clone https://github.com/<your-id>/<repo-name>.git
cd development/backend
./gradlew bootRun   # Spring Boot 실행

cd ../sencha
sencha app watch    # Ext JS 실행




작업 내용 백업

# 1. 현재 변경된 파일 확인
git status

# 2. 수정/추가된 파일 스테이징
git add .

# 3. 커밋 메시지와 함께 저장
git commit -m "작업 내용 설명: 예) tban1002 화면 수정, API 연동 추가"

# 4. 원격 저장소로 푸시
git push origin main









