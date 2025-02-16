



docker build -t food-for-you-backend .

docker run -p 8080:8080 food-for-you-backend



docker build -t food-for-you-frontend .

docker run -p 3000:3000 food-for-you-frontend


docker build -t food-for-you-ia .

docker run -p 5000:5000 food-for-you-ia



docker compose up
