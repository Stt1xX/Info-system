cd ../shared_module
call gradlew build
call gradlew publishToMavenLocal

cd ../consumer
call gradlew build

cd ../frontend
call npm install
call npm run build
cd ../backend
call gradlew build

docker-compose up --build -d
