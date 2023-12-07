// Copyright 2021 The Chromium Authors
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef COMPONENTS_SUBRESOURCE_FILTER_ANDROID_CONFIG_SUBRESOURCE_FILTER_FETCHING_H_
#define COMPONENTS_SUBRESOURCE_FILTER_ANDROID_CONFIG_SUBRESOURCE_FILTER_FETCHING_H_

#include <string>

namespace subresource_filter {
bool IsInitializedFromConfig();
bool DeleteUnindexedFile();
} // namespace subresource_filter

#endif // COMPONENTS_SUBRESOURCE_FILTER_ANDROID_CONFIG_SUBRESOURCE_FILTER_FETCHING_H_