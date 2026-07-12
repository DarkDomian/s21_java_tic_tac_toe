# <span><a href="https://21-school.ru/?utm_source=school21&utm_medium=student_nino&utm_campaign=trelawnm___"><img style="height: 1em;" src="misc/heart.gif"></a></span> The 21 School Java Tic Tac Toe with Spring

Tic Tac Toe is a fourth project of Java bootcamp into 21 School curiclium.

![AI generated banner for project](misc/s21_tic_tac_toe.jpg)

<!-- to making such "bunner" just know - it's aspect ratio is 11/3 -->

<!-- 
## Gradle Multi-module Cheat Sheet

**Project structure:** root project `Smart_Utilities` with subprojects `exercise1` . . . `exercise10`.
Run all commands from the **project root** directory.


> [!TIP]
> For building on Windows, just replace `./gradlew` with `gradlew.bat`.

All available tasks can be observed by running `./gradlew tasks`, for any further information pass `--help` to the command you are interested in.

Most commonly used patterns:
```bash
# All modules
./gradlew build
./gradlew test
./gradlew clean

# Single module (change exercise3 to the chosen one)
./gradlew :exercise3:build
./gradlew :exercise3:test
./gradlew :exercise3:clean
./gradlew :exercise3:run           # run the main class of this module

# Single test class in a single module
./gradlew :exercise4:test --tests "full.package.path.and.Class"
```

> [!TIP]
> - For skipping tests just add `-x test` to the prompt. `-x` means `--exclude-task`.
> - The colon `:` before the module name is *mandatory* for subproject paths.
> - For running a single test class, add it to the `test` task using `--tests "<full class name (package + class)>"`, for example `--tests "dev.trelawnm.exercises.triangle.TrianglePerimeterTest"`

<!--
```
for i in {0..10}; do
  cp -r app exercise$i
done

echo """
include($(for i in {1..9}; do echo -n "\"exercise$i\","; done))
""" >> settings.gradle.kts
```
--->