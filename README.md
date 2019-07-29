# PassField
Password EditText for Android with supporting custom font asset src


<img src="https://github.com/TalebRafiepour/PassField/blob/master/screenshot.jpg" width="350" height="68" alt="passField">


### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### Step 2. Add the dependency

```
dependencies {
	        implementation 'com.github.TalebRafiepour:PassField:1.0.0'
	}
```

### sample

```
<com.taleb.passfield.PasswordTextField
            style="@style/Input"
            android:layout_gravity="center"
            android:layoutDirection="ltr"
            android:layout_margin="16dp"
            app:fontAssetSrc="fonts/Action_Man_Extended.ttf"
            app:showDrawable="@drawable/ic_password_visible_24dp"
            app:hideDrawable="@drawable/ic_password_hidden_24dp"
            app:drawableTintCompat="?attr/colorAccent"/>
```
