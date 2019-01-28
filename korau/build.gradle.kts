import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeCompilation

for (target in listOf("macosX64", "linuxX64", "iosX64", "iosArm32", "iosArm64")) {
    (kotlin.targets[target].compilations["main"] as KotlinNativeCompilation).apply {
        cinterops.apply {
            maybeCreate("minimp3").apply {
            }
            maybeCreate("stb_vorbis").apply {
            }
        }
    }
}

for (target in listOf("mingwX64")) {
    (kotlin.targets[target].compilations["main"] as KotlinNativeCompilation).apply {
        cinterops.apply {
            maybeCreate("win32_winmm").apply {
            }
        }
    }
}

for (target in listOf("linuxX64")) {
    (kotlin.targets[target].compilations["main"] as KotlinNativeCompilation).apply {
        cinterops.apply {
            maybeCreate("linux_OpenAL").apply {
            }
        }
    }
}

for (target in listOf("macosX64")) {
    (kotlin.targets[target].compilations["main"] as KotlinNativeCompilation).apply {
        cinterops.apply {
            maybeCreate("mac_OpenAL").apply {
            }
        }
    }
}

if (System.getProperty("idea.version") != null) {
    if (org.apache.tools.ant.taskdefs.condition.Os.isFamily(org.apache.tools.ant.taskdefs.condition.Os.FAMILY_MAC)) {
        for (target in listOf("nativeCommon", "nativePosix")) {
            (kotlin.targets[target].compilations["main"] as KotlinNativeCompilation).apply {
                cinterops.apply {
                    maybeCreate("minimp3").apply {}
                    maybeCreate("stb_vorbis").apply {}
                    maybeCreate("AVFoundation").apply {}
                    maybeCreate("mac_OpenAL").apply {}
                }
            }
        }
    }
}

/*
kotlin.targets.macosX64.compilations.main.cinterops {
    //CoreAudio {}
    //CoreAudioKit {}
    //AudioToolbox {}

    // error: compilation failed: external function @kotlin.Deprecated public open external fun new(): platform.AVFoundation.AVCaptureBracketedStillImageSettings? defined in platform.AVFoundation.AVCaptureBracketedStillImageSettingsMeta[SimpleFunctionDescriptorImpl@649a76c1] must have @SymbolName, @Intrinsic or @ObjCMethod annotation
    // * Source files: AVFoundation.kt
    // * Compiler version info: Konan: 1.1-rc2-5686 / Kotlin: 1.3.20
    // * Output kind: LIBRARY
    // exception: java.lang.Error: external function @kotlin.Deprecated public open external fun new(): platform.AVFoundation.AVCaptureBracketedStillImageSettings? defined in platform.AVFoundation.AVCaptureBracketedStillImageSettingsMeta[SimpleFunctionDescriptorImpl@649a76c1] must have @SymbolName, @Intrinsic or @ObjCMethod annotation

    //AVFoundation {}


    mac_OpenAL {}
}

*/
