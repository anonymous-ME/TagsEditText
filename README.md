# Android TagsEditText View for creating tags with EditText

## How to use ##


Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
	repositories {
		maven { url "https://jitpack.io" }
	}
}
```
Step 2. Add the dependency
```groovy
dependencies {
	implementation 'com.github.anonymous-ME:TagsEditText:0.5.0'
}
```
Step 3. Add TagsEditText to your layout file
```xml
    <affan.ahmad.tags.TagsEditText
            android:animateLayoutChanges="true"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
```
## Screenshot ##
![Screenshot](Screenshot.jpg)
