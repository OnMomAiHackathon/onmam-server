# AWS Secrets Manager에서 환경 변수 조회
SECRET_NAME="myBlogDatabase"
REGION="ap-northeast-2"
# jq가 설치되어 있는지 확인 (설치되지 않은 경우 설치)
if ! command -v jq &> /dev/null
then
    echo "jq could not be found, installing..."
    sudo apt-get update
    sudo apt-get install -y jq
fi
# AWS CLI를 사용하여 비밀 값을 조회
SECRET=$(aws secretsmanager get-secret-value --secret-id $SECRET_NAME --region $REGION --query SecretString --output text)

# 조회된 비밀 값을 환경 변수로 설정
export DATABASE_URL=$(echo $SECRET | jq -r .DATABASE_URL)
export DATABASE_USERNAME=$(echo $SECRET | jq -r .DATABASE_USERNAME)
export DATABASE_PASSWORD=$(echo $SECRET | jq -r .DATABASE_PASSWORD)

# s3 파일업로드를 위한 환경 변수
export AWS_ACCESS_KEY_ID=$(echo $SECRET | jq -r .AWS_ACCESS_KEY_ID)
export AWS_SECRET_ACCESS_KEY=$(echo $SECRET | jq -r .AWS_SECRET_ACCESS_KEY)
export AWS_REGION=$(echo $SECRET | jq -r .AWS_REGION)
export S3_BUCKET=$(echo $SECRET | jq -r .S3_BUCKET)

# gpt 환경변수
export OPENAI_API_KEY=$(echo $SECRET | jq -r .OPENAI_API_KEY)
export OPENAI_DALLE_ENDPOINT=$(echo $SECRET | jq -r .OPENAI_DALLE_ENDPOINT)
export OPENAI_TRANSCRIPTIONS_ENDPOINT=$(echo $SECRET | jq -r .OPENAI_TRANSCRIPTIONS_ENDPOINT)
export OPENAI_GPT_ENDPOINT=$(echo $SECRET | jq -r .OPENAI_GPT_ENDPOINT)

# kakao 환경변수
export KAKAO_CLIENT_ID=$(echo $SECRET | jq -r .KAKAO_CLIENT_ID)
export KAKAO_CLIENT_SECRET=$(echo $SECRET | jq -r .KAKAO_CLIENT_SECRET)

# Spring Boot 애플리케이션 디렉터리
SPRINGBOOT_DIR="/opt/onmom/springboot"

# 이전에 실행된 Spring Boot 애플리케이션 종료
CURRENT_PID=$(pgrep -f 'onmom-server.*.jar')
if [ -n "$CURRENT_PID" ]; then
    echo "Stopping running Spring Boot application"
    kill -15 "$CURRENT_PID"
fi

# 잠시 대기
sleep 5

# 로그 디렉토리 생성
mkdir -p $SPRINGBOOT_DIR/logs

# 새 Spring Boot 애플리케이션 배포
JAR_NAME=$(ls $SPRINGBOOT_DIR | grep '.jar' | tail -n 1)
echo "Deploying $JAR_NAME"
# nohup으로 애플리케이션 실행 시 로그 파일 생성
nohup java -Duser.timezone=Asia/Seoul -jar $SPRINGBOOT_DIR/$JAR_NAME --spring.profiles.active=prod > $SPRINGBOOT_DIR/logs/onmomLog.log 2>&1 &
