# Kotlinx Serialization
-keepattributes *Annotation*,InnerClasses,EnclosingMethod,Signature,Exceptions
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# Logback / Jakarta Servlet (Suppress warnings for missing classes not used in Android)
-dontwarn jakarta.servlet.ServletContainerInitializer
