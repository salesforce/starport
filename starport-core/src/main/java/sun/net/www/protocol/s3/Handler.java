/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package sun.net.www.protocol.s3;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class Handler extends URLStreamHandler {

    protected URLConnection openConnection(URL url) throws IOException {

        return new URLConnection(url) {

            AmazonS3 s3Client;

            @Override
            public InputStream getInputStream() throws IOException {
                String bucket = url.getHost();
                String key = url.getPath().substring(1);
                return s3Client.getObject(bucket, key).getObjectContent();
            }

            @Override
            public void connect() throws IOException {
                s3Client = AmazonS3ClientBuilder.defaultClient();
            }

        };
    }
}
