package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;

import com.expansion.lg.kimaru.expansion.tables.EducationTable;
import com.expansion.lg.kimaru.expansion.tables.RecruitmentTable;
import com.expansion.lg.kimaru.expansion.tables.UserTable;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

/**
 * Created by kimaru on 3/22/17.
 */

public class HttpServer {
    private AsyncHttpServer server = new AsyncHttpServer();
    private AsyncServer asyncServer = new AsyncServer();

    private AsyncHttpClient client = new AsyncHttpClient(asyncServer);

    final String SERVER_URL = "http://192.168.43.1";
    final int SERVER_PORT = 8090;
    final String RECRUIRMENT_URL = "recruitments";
    private static String url;
    Context context;

    public HttpServer(Context context){
        this.context = context;
    }

    public void startServer(){
        server.get("/"+RECRUIRMENT_URL, new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                RecruitmentTable recruitmentData = new RecruitmentTable(context);
                response.send(recruitmentData.getRecruitmentJson());
            }
        });
        server.get("/users", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                UserTable userTable = new UserTable(context);
                response.send(userTable.getUsersJson());
            }
        });
        server.get("/education", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                EducationTable educationTable = new EducationTable(context);
                response.send(educationTable.getEducationJson());
            }
        });
        server.listen(asyncServer, SERVER_PORT);

    }
}
