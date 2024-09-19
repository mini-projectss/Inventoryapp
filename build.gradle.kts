// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    // Use the latest version of the Google Services plugin here
    id("com.google.gms.google-services") version "4.4.2" apply false
}
