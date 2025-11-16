/*-
 *  Copyright (C) 2013 Sam Malone
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.peterbaldwin.vlcremote.net;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Base64;
import java.nio.charset.StandardCharsets;
import org.peterbaldwin.client.android.vlcremote.R;
import org.peterbaldwin.vlcremote.model.Server;

/**
 *
 * @author Sam Malone
 */
public class ServerConnectionTest extends AsyncTask<Server, Void, Integer> {
    
    private final static String TEST_PATH = "/requests/status.xml";

    private Context context;
    
    public ServerConnectionTest(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Server... servers) {
        if(servers == null || servers.length != 1) {
            return -1;
        }
        URL url;
        try {
            url = new URL("http://" + servers[0].getUri().getAuthority() + TEST_PATH);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            try {
                String u = servers[0].getUser();
                String p = servers[0].getPassword();
                if(u != null && p != null) {
                    String creds = u + ":" + p;
                    String basic = Base64.encodeToString(creds.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
                    connection.setRequestProperty("Authorization", "Basic " + basic);
                }
                return connection.getResponseCode();
            } finally {
                connection.disconnect();
            }
        } catch (IOException ex) {
            
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer result) {
        switch(result) {
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                Toast.makeText(context, R.string.server_unauthorized, Toast.LENGTH_SHORT).show();
                break;
            case HttpURLConnection.HTTP_FORBIDDEN:
                Toast.makeText(context, R.string.server_forbidden, Toast.LENGTH_SHORT).show();
                break;
            case HttpURLConnection.HTTP_OK:
                Toast.makeText(context, R.string.server_ok, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
        }
    }
    
}
