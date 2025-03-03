plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ll"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    //postgreSQL & PostGIS
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.hibernate:hibernate-spatial:6.2.13.Final")

    //websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    //validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // SpringBoot Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // OAuth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // JSON Simple
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    //S3 bucket
    implementation(platform("software.amazon.awssdk:bom:2.24.0"))
    implementation("software.amazon.awssdk:s3")

    //swegger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    //kafka
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    //docker-compose
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
