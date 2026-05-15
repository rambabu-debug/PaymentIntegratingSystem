# Deployment Guide - Payment Integration System

## Local Development Setup

### Quick Start (5 minutes)

```bash
# 1. Clone/Download the project
cd PaymentIntegrationSystem

# 2. Set environment variables (Windows)
set STRIPE_SECRET_KEY=sk_test_your_key
set JWT_SECRET=your-secret-key-here
set FIREBASE_PROJECT_ID=your-firebase-id

# 3. Place Firebase service account key
# Copy serviceAccountKey.json to project root

# 4. Run the application
.\gradlew.bat bootRun
```

The API will be available at `http://localhost:8080`

---

## Docker Deployment

### Build and Run with Docker

```bash
# Build image
docker build -t payment-integration-system .

# Run container
docker run -p 8080:8080 \
  -e STRIPE_SECRET_KEY=sk_test_your_key \
  -e JWT_SECRET=your-secret-key \
  -e FIREBASE_PROJECT_ID=your-firebase-id \
  -v /path/to/serviceAccountKey.json:/app/serviceAccountKey.json:ro \
  payment-integration-system
```

### Using Docker Compose

```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f payment-app

# Stop services
docker-compose down
```

---

## AWS Deployment (EC2)

### Prerequisites
- AWS EC2 instance (Ubuntu 22.04 LTS recommended)
- Java 17 installed
- Security group with ports 8080, 443 open

### Step-by-Step

```bash
# 1. Connect to EC2 instance
ssh -i your-key.pem ubuntu@your-instance-ip

# 2. Install Java 17
sudo apt update
sudo apt install openjdk-17-jdk-headless -y

# 3. Clone repository
git clone <your-repo-url>
cd PaymentIntegrationSystem

# 4. Set environment variables
export STRIPE_SECRET_KEY=sk_test_xxx
export JWT_SECRET=your-secret-key
export FIREBASE_PROJECT_ID=your-firebase-id

# 5. Build application
./gradlew build -x test

# 6. Run as service
# Create systemd service file
sudo nano /etc/systemd/system/payment-app.service
```

**Create `/etc/systemd/system/payment-app.service`:**

```ini
[Unit]
Description=Payment Integration System
After=network.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/home/ubuntu/PaymentIntegrationSystem
Environment="STRIPE_SECRET_KEY=sk_test_xxx"
Environment="JWT_SECRET=your-secret"
Environment="FIREBASE_PROJECT_ID=your-firebase-id"
ExecStart=/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar build/libs/PaymentIntegrationSystem-1.0.0.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
# Enable and start service
sudo systemctl daemon-reload
sudo systemctl enable payment-app
sudo systemctl start payment-app

# Check status
sudo systemctl status payment-app

# View logs
sudo journalctl -u payment-app -f
```

---

## Kubernetes Deployment

### Create Kubernetes Manifests

**deployment.yaml:**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: payment-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: payment-app
  template:
    metadata:
      labels:
        app: payment-app
    spec:
      containers:
      - name: payment-app
        image: your-registry/payment-integration-system:latest
        ports:
        - containerPort: 8080
        env:
        - name: STRIPE_SECRET_KEY
          valueFrom:
            secretKeyRef:
              name: payment-secrets
              key: stripe-key
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: payment-secrets
              key: jwt-secret
        - name: FIREBASE_PROJECT_ID
          valueFrom:
            secretKeyRef:
              name: payment-secrets
              key: firebase-id
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /api/auth/me
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /api/auth/me
            port: 8080
          initialDelaySeconds: 10
          periodSeconds: 5
```

**service.yaml:**
```yaml
apiVersion: v1
kind: Service
metadata:
  name: payment-app-service
spec:
  selector:
    app: payment-app
  type: LoadBalancer
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
```

**Deploy:**
```bash
# Create secrets
kubectl create secret generic payment-secrets \
  --from-literal=stripe-key=sk_test_xxx \
  --from-literal=jwt-secret=your-secret \
  --from-literal=firebase-id=your-firebase-id

# Deploy
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml

# Check deployment
kubectl get deployments
kubectl get pods
kubectl get services
```

---

## Heroku Deployment

### Prerequisites
- Heroku CLI installed
- Heroku account

### Steps

```bash
# 1. Login to Heroku
heroku login

# 2. Create Heroku app
heroku create payment-integration-system

# 3. Set environment variables
heroku config:set STRIPE_SECRET_KEY=sk_test_xxx
heroku config:set JWT_SECRET=your-secret-key
heroku config:set FIREBASE_PROJECT_ID=your-firebase-id

# 4. Add Firebase credentials
heroku config:set GOOGLE_APPLICATION_CREDENTIALS=/app/serviceAccountKey.json

# 5. Upload service account key
# Create file: heroku/serviceAccountKey.json

# 6. Deploy
git push heroku main

# 7. View logs
heroku logs -t

# 8. Check app status
heroku apps:info
```

**Create `Procfile`:**
```
web: java -jar build/libs/PaymentIntegrationSystem-1.0.0.jar
```

---

## Production Checklist

- [ ] Update Stripe keys to live keys
- [ ] Update JWT secret to strong 32+ character string
- [ ] Enable HTTPS/SSL certificates
- [ ] Configure PostgreSQL database
- [ ] Set up database backups
- [ ] Configure Firebase security rules
- [ ] Set up monitoring and alerts
- [ ] Configure rate limiting
- [ ] Set up CDN for static assets
- [ ] Enable CORS for production domain
- [ ] Configure logging aggregation (CloudWatch, ELK)
- [ ] Set up health checks
- [ ] Configure auto-scaling
- [ ] Set up backup and disaster recovery

---

## Monitoring & Logging

### CloudWatch (AWS)

```bash
# Push logs to CloudWatch
aws logs create-log-group --log-group-name /payment-app/logs
```

### Application Logs Configuration

Add to `application.yml`:
```yaml
logging:
  level:
    root: INFO
    todo.tutorials: DEBUG
    com.stripe: DEBUG
    com.google: DEBUG
  file:
    name: logs/app.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```

---

## Scaling & Load Balancing

### With Docker Compose

```yaml
version: '3.8'
services:
  app1:
    build: .
    ports:
      - "8081:8080"
  app2:
    build: .
    ports:
      - "8082:8080"
  nginx:
    image: nginx:latest
    ports:
      - "80:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
```

### Nginx Configuration

```nginx
upstream payment_apps {
    server app1:8080;
    server app2:8080;
    server app3:8080;
}

server {
    listen 80;
    server_name _;

    location / {
        proxy_pass http://payment_apps;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

---

## Backup & Recovery

### Database Backup

```bash
# PostgreSQL backup
pg_dump -U postgres payment_db > backup.sql

# Restore
psql -U postgres < backup.sql
```

### Firebase Backup

```bash
# Export Firestore
gcloud firestore export gs://your-bucket/backup

# Import Firestore
gcloud firestore import gs://your-bucket/backup
```

---

## Troubleshooting Deployments

### Application won't start
```bash
# Check logs
docker logs container_id

# Verify environment variables
echo $STRIPE_SECRET_KEY

# Test connectivity
curl http://localhost:8080/api/auth/me
```

### High memory usage
```bash
# Increase heap size
java -Xmx1024m -Xms512m -jar PaymentIntegrationSystem-1.0.0.jar
```

### Database connection issues
```bash
# Test connection
psql -h localhost -U postgres -d payment_db

# Check credentials in application.yml
```

---

## Performance Tuning

### Database Optimization

```sql
-- Create indexes
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_payment_merchant ON payment_transactions(merchant_id);
CREATE INDEX idx_payment_status ON payment_transactions(status);
```

### Application Optimization

```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

---

## Security Best Practices

1. **HTTPS Only**: Always use SSL/TLS in production
2. **Environment Variables**: Never commit secrets
3. **Database Encryption**: Enable at-rest encryption
4. **API Keys Rotation**: Rotate Stripe keys regularly
5. **Rate Limiting**: Implement to prevent abuse
6. **Input Validation**: Validate all inputs
7. **CORS Configuration**: Restrict to known domains
8. **Audit Logging**: Log all sensitive operations

---

**Last Updated**: April 5, 2026

