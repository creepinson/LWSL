# Light Weight Socket Library  
[![CircleCI](https://circleci.com/gh/StijnSimons/LWSL/tree/master.svg?style=svg)](https://circleci.com/gh/StijnSimons/LWSL/tree/master)  

LWSL is an open source socket library made in Java.  
Everyone can contribute by making pull requests.  
If you have any issues / new features, present them in the issues tab.

## Usage
**Project unfinished**
Please wait for the first release.

## Installation
If your project is using Maven or Gradle, check the tutorials below.

### Maven

```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
```xml
<dependency>
	<groupId>com.github.StijnSimons</groupId>
	<artifactId>LWSL</artifactId>
	<version>VERSIONHERE</version>
</dependency>
```
Don't forget to fill in the version where "VERSIONHERE" is present.

### Gradle

```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```gradle
	dependencies {
		implementation 'com.github.StijnSimons:LWSL:VERSIONHERE'
	}
```
Don't forget to fill in the version where "VERSIONHERE" is present.