// Copyright 2021 The Chromium Authors
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "components/subresource_filter/android_config/subresource_filter_fetching.h"

#include <string>

#include "base/android/jni_android.h"
#include "components/subresource_filter/android_config/subresource_filter_fetching_jni_headers/SubresourceFilterFetching_jni.h"

namespace subresource_filter {

bool IsInitializedFromConfig() {
  JNIEnv* env = base::android::AttachCurrentThread();
  return Java_SubresourceFilterFetching_isInitialized(env);
}

bool DeleteUnindexedFile() {
  JNIEnv* env = base::android::AttachCurrentThread();
  return Java_SubresourceFilterFetching_deleteUnindexedFile(env);
}

}
