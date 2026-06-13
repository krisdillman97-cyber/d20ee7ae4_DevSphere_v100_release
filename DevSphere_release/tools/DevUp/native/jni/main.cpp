// DevSphere Native Layer — JNI bridge
#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_glypheral_devsphere_NativeBridge_getVersion(JNIEnv* env, jobject) {
    return env->NewStringUTF("DevSphere/1.0 NDK/27");
}
