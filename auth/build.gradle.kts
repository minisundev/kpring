plugins {
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // kotest
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.0")

    // default test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

    // db
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("io.asyncer:r2dbc-mysql")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    // dev tool
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}