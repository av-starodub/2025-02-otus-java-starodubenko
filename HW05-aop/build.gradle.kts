dependencies {
    implementation("org.slf4j:slf4j-api")
    implementation("ch.qos.logback:logback-classic")

    testImplementation("org.assertj:assertj-core")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
