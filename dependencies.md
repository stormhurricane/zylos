# Projekt-Abhängigkeiten

## 🖥️ Backend (`build.gradle`)

### Implementation
* **org.springframework.boot:spring-boot-starter-web**
* **org.springframework.boot:spring-boot-starter-jdbc**
* **org.springframework.boot:spring-boot-starter-data-jpa:2.4.5**
* **org.springframework.boot:spring-boot-starter-mail**
* **com.sun.mail:javax.mail:1.6.2**
* **javax.activation:activation:1.1.1**

### Runtime Only
* **com.h2database:h2**
* **mysql:mysql-connector-java:8.0.33**

### Test Implementation
* **org.springframework.boot:spring-boot-starter-test**
* **org.junit.jupiter:junit-jupiter-api:5.3.1**
* **org.junit.platform:junit-platform-runner:1.8.0-M1**
* **org.junit.vintage:junit-vintage-engine:5.7.2**
* **com.h2database:h2:1.4.200**
* **org.mockito:mockito-core:2.+**
* **org.mockito:mockito-junit-jupiter:2.21.0**

### Test Runtime Only
* **org.junit.jupiter:junit-jupiter-engine:5.3.1**

---

## 📱 Frontend (`build.gradle`)

### Implementation
* **com.squareup.retrofit2:retrofit:2.5.0**
* **com.google.code.gson:gson:2.8.6**
* **com.squareup.retrofit2:converter-gson:2.4.0**
* **com.squareup.retrofit2:converter-jackson:2.4.0**
* **commons-codec:commons-codec:1.5**
* **com.squareup.okhttp3:logging-interceptor:4.2.1**

### Test Implementation
* **org.junit.jupiter:junit-jupiter-api:5.7.0**

### Test Runtime Only
* **org.junit.jupiter:junit-jupiter-engine:5.7.0**

---

Stand: 05.04.2025