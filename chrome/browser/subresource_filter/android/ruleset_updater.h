// Copyright 2023-2024 GrapheneOS
// Use of this source code is governed by a GPL-2.0-style license that can be
// found in the LICENSE file.
//
// Copyright 2022-2023 Zoraver Kang, who initially wrote this class
// and did the preliminary research needed to support content filtering updates.

#ifndef CHROME_BROWSER_SUBRESOURCE_FILTER_ANDROID_RULESET_UPDATER_H_
#define CHROME_BROWSER_SUBRESOURCE_FILTER_ANDROID_RULESET_UPDATER_H_

#include <string>

namespace subresource_filter {
void UpdateRulesetFromConfig(std::string version, std::string path);
} // namespace subresource_filter

#endif // CHROME_BROWSER_SUBRESOURCE_FILTER_ANDROID_RULESET_UPDATER_H_