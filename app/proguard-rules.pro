# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep all class in local
-keep class com.example.logiclyst.data.local.** { *; }

# Keep all class in remote
-keep class com.example.logiclyst.data.remote.** { *; }

# Room safe while runtime
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public <init>(...);
}
-keep class androidx.room.Entity
-keep class androidx.room.Dao
-keep class androidx.room.Database
-keep class androidx.room.PrimaryKey
-keep class androidx.room.Query

# Keep annotation SerializedName gson
-keepattributes Signature, *Annotation*, EnclosingMethod
-keep class com.google.gson.annotations.SerializedName { *; }