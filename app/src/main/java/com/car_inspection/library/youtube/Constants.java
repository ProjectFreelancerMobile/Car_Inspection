/*
 * Copyright (c) 2013 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.car_inspection.library.youtube;

/**
 * @author Ibrahim Ulukaya <ulukaya@google.com>
 *         <p/>
 *         This class hold constants.
 */
public class Constants {
    public static final String ACCOUNT_KEY = "accountName";
    public static final String MESSAGE_KEY = "message";
    public static final String YOUTUBE_ID = "youtubeId";
    public static final String YOUTUBE_WATCH_URL_PREFIX = "http://www.youtube.com/watch?v=";
    static final String REQUEST_AUTHORIZATION_INTENT = "com.google.example.yt.RequestAuth";
    static final String REQUEST_AUTHORIZATION_INTENT_PARAM = "com.google.example.yt.RequestAuth.param";
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    private static final int REQUEST_GMS_ERROR_DIALOG = 1;
    private static final int REQUEST_ACCOUNT_PICKER = 2;
    private static final int REQUEST_AUTHORIZATION = 3;
    private static final int RESULT_PICK_IMAGE_CROP = 4;
    private static final int RESULT_VIDEO_CAP = 5;
    private static final int REQUEST_DIRECT_TAG = 6;
    public static final int MAX_KEYWORD_LENGTH = 30;
    public static final String DEFAULT_KEYWORD = "ytdl";
    // A playlist ID is a string that begins with PL. You must replace this string with the correct

    // playlist ID for the app to work
    public static final String UPLOAD_PLAYLIST = "PLZzKgGPkrMyeVBr9EMFY_Dlq4cio9xABB";
    public static final String APP_NAME = "Carmudi Inspection App";
}
