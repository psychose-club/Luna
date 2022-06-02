/*
 * Copyright © 2022 psychose.club
 * Discord: https://www.psychose.club/discord
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.psychose.luna.api.iloveradio;

import club.psychose.luna.utils.logging.CrashLog;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLHandshakeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * This class handles the ILoveRadio streams fetching.
 */

public final class ILoveRadioFetcher {
    private final ArrayList<ILoveRadioStream> iLoveRadioStreamsArrayList = new ArrayList<>();

    // This method fetches the streams from the ILoveRadio website.
    public boolean fetchStreams () {
        // Connects to the ILoveRadio website.
        HttpGet httpGet = new HttpGet("https://ilovemusic.de/streams/");

        // Spoofs the user-agent.
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");

        // Creates a cookie store.
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        HttpContext httpContext = new BasicHttpContext();

        // Sets the attribute.
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, basicCookieStore);

        // Creates a http client.
        // TODO: Find solution for the SSLHandshakeException.
        try (CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build()) {
            // Connects to the URL and looks if 200 got returned.
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet, httpContext);
            HttpEntity httpEntity = httpResponse.getEntity();

            // Checks if the response is not null.
            if (httpEntity != null) {
                // Reads the website content.
                try (InputStream inputStream = httpEntity.getContent()) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    // Clears the current fetched streams.
                    this.iLoveRadioStreamsArrayList.clear();

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        // Checks if the line contains a stream url.
                        if (line.contains("https://streams.ilovemusic.de/")) {
                            // Splits the streams.
                            line = line.replaceAll("\u0026", "&").replaceAll(" ", "").trim();
                            String[] splitStreams = line.split("<br/>");

                            String streamName = "?";
                            String streamURL = "";

                            // Checks the streams.
                            for (String stream : splitStreams) {
                                // If the line contains a .m3u stream it'll fetch the radio name of it.
                                // If the line contains a .mp3 stream it'll fetch the stream url of it.
                                if (stream.contains(".m3u")) {
                                    streamName = stream.split("<p><ahref=\"")[1].split(".m3u\"target=\"_blank\">")[0].trim().replaceAll("https://www.ilovemusic.de/", "").replaceAll("https://ilovemusic.de/", "");
                                } else if (stream.contains(".mp3")) {
                                    streamURL = stream.split("<ahref=\"")[1].split("\"target=\"")[0].trim();
                                }
                            }

                            // Creates the stream and adds it to the ArrayList.
                            ILoveRadioStream iLoveRadioStream = new ILoveRadioStream(streamName, streamURL);

                            if (!(this.iLoveRadioStreamsArrayList.contains(iLoveRadioStream)))
                                this.iLoveRadioStreamsArrayList.add(iLoveRadioStream);
                        }
                    }

                    bufferedReader.close();
                    inputStream.close();
                    closeableHttpClient.close();

                    return true;
                } catch (IOException ioException) {
                    CrashLog.saveLogAsCrashLog(ioException);
                }
            }
        } catch (SSLHandshakeException ignored) {} catch (IOException ioException) {
            CrashLog.saveLogAsCrashLog(ioException);
        }

        return false;
    }

    // This method returns the valid ILoveRadio streams.
    public ArrayList<ILoveRadioStream> getILoveRadioStreamsArrayList () {
        return this.iLoveRadioStreamsArrayList;
    }
}