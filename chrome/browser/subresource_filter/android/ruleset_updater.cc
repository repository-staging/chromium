// Copyright 2023-2024 GrapheneOS
// Use of this source code is governed by a GPL-2.0-style license that can be
// found in the LICENSE file.
//
// Copyright 2022-2023 Zoraver Kang, who initially wrote this class
// and did the preliminary research needed to support content filtering updates.

#include "chrome/browser/subresource_filter/android/ruleset_updater.h"

#include "base/android/jni_android.h"
#include "base/android/jni_string.h"
#include "chrome/browser/browser_process.h"
#include "chrome/browser/subresource_filter/android/jni_headers/RulesetUpdater_jni.h"
#include "components/subresource_filter/content/shared/browser/ruleset_service.h"
#include "components/subresource_filter/core/browser/ruleset_version.h"

using base::android::ConvertJavaStringToUTF8;
using base::android::ConvertUTF8ToJavaString;
using base::android::JavaParamRef;
using base::android::ScopedJavaLocalRef;

namespace subresource_filter {

static void JNI_RulesetUpdater_Update(JNIEnv *env,
    const JavaParamRef<jstring>& junindexed_content_version,
    const JavaParamRef<jstring>& junindexed_ruleset_path) {
  const std::string unindexed_content_version =
      ConvertJavaStringToUTF8(env, junindexed_content_version);
  const std::string unindexed_ruleset_path =
      ConvertJavaStringToUTF8(env, junindexed_ruleset_path);

  UpdateRulesetFromConfig(unindexed_content_version, unindexed_ruleset_path);
}

// static
void UpdateRulesetFromConfig(std::string version, std::string path) {
  UnindexedRulesetInfo ruleset_info;
  ruleset_info.content_version = version;
  // We know that unindexed_ruleset_path is UTF8 since it is the output of
  // ConvertJavaStringToUTF8().
  ruleset_info.ruleset_path =
      base::FilePath::FromUTF8Unsafe(path);

  RulesetService *ruleset_service =
      g_browser_process->subresource_filter_ruleset_service();
  if (ruleset_service) {
    ruleset_service->IndexAndStoreAndPublishRulesetIfNeeded(ruleset_info);
  }
}

static ScopedJavaLocalRef<jstring> JNI_RulesetUpdater_Version(JNIEnv *env) {
  RulesetService *ruleset_service =
      g_browser_process->subresource_filter_ruleset_service();
  if (!ruleset_service) return ScopedJavaLocalRef<jstring>(nullptr);

  return ConvertUTF8ToJavaString(env,
      ruleset_service->GetMostRecentlyIndexedVersion().content_version);
}

} // namespace subresource_filter
