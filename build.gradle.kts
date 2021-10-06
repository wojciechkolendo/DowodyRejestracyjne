buildscript { setup() }

allprojects { addRepositories() }

tasks.register("clean", Delete::class) { delete(rootProject.buildDir) }