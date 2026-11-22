# Proguard rules for Growth Up
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Room Database & Entities
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keep class com.growthup.app.data.model.** { *; }
-keep class com.growthup.app.data.local.** { *; }
-keep class com.growthup.app.domain.repository.** { *; }

# Coroutines
-dontwarn kotlinx.coroutines.**
-keepclassmembers class kotlinx.coroutines.** { *; }
