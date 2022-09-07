# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/embed/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#AWS Amazon library files
# Class names are needed in reflection
-keep class com.amazonaws.** { *; }
-keepnames class com.amazonaws.** { *; }
-dontwarn com.amazonaws.**
-dontwarn com.fasterxml.**
-keep class org.apache.commons.logging.**               { *; }
-keep class com.amazonaws.services.sqs.QueueUrlHandler  { *; }
-keep class com.amazonaws.javax.xml.transform.sax.*     { public *; }
-keep class com.amazonaws.javax.xml.stream.**           { *; }
-keep class com.amazonaws.services.**.model.*Exception* { *; }
-keep class org.codehaus.**                             { *; }
-keepattributes Signature,*Annotation*

-dontwarn javax.xml.stream.events.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.apache.commons.logging.impl.**
-dontwarn org.apache.http.conn.scheme.**

#SSL
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**

#DAGGER library rules
-dontwarn com.google.errorprone.annotations.**

#duplicate library compenents
-dontnote org.apache.http.**
-dontnote android.net.http.**
-dontnote com.android.internal.http.**
-dontnote org.apache.commons.**
-dontnote android.net.compatibility.WebAddress

#OKHTTP library
-dontwarn okio.**
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.* { *; }
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-keepattributes Signature -keepattributes Annotation -keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.** -dontwarn okio.**

#Wave
-keep class com.karru.util.WaveDrawableRequest {*;}

#RETROFIT library
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclassmembernames interface * {
        @retrofit.http.* <methods>;
}


#ADNROID WEBKIT
-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

# Twilio Client
-keep class com.twilio.** { *; }

# Apache HttpClient
-dontwarn org.apache.http.**

#for appsflyer
-dontwarn com.android.installreferrer
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-ignorewarnings
