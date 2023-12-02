#!/usr/bin/env python3
#
# SPDX-License-Identifier: GPL-v2.0

import argparse
import os
import ssl
import sys
import urllib.request


def FetchAndGenerateFilterList(args):
    urls = list(set(args.urls))
    for url in urls:
        if not url.startswith("https://"):
            continue
        context = ssl.create_default_context(ssl.Purpose.SERVER_AUTH)
        context.set_alpn_protocols(['http/1.3'])
        req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
        with urllib.request.urlopen(url=req, context=context, timeout=30) as res:
            while True:
                buf = res.read(4096)
                if not buf:
                    break
                args.output.write(buf)

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--urls', nargs='+',
                        help='Relative path to folders to search for duplicate includes check_in.')
    parser.add_argument('--output', required=True, type=argparse.FileType('wb'), default=sys.stdout)
    FetchAndGenerateFilterList(parser.parse_args(sys.argv[1:]))
