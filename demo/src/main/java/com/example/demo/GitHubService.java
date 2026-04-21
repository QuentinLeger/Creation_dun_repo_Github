package com.example.demo;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GitHubService {
    private final String token;

    public GitHubService (String token){
        this.token = token;
    }

    public int createRepository (String name, String description, boolean isPrivate) throws Exception{
        URL url = new URL("https://api.github.com/user/repos");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization","Bearer" + token);
        connection.setRequestProperty("Accept","application/vnd.github+json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String json = """
        {
            "name": "%s",
                "description": "%s",
                "private": %s
        }
        """.formatted(name, description, isPrivate);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(json.getBytes());
        }
        return connection.getResponseCode();
    }
}
