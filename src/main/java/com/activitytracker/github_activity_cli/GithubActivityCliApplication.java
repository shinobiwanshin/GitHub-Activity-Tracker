package com.activitytracker.github_activity_cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class GithubActivityCliApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GithubActivityCliApplication.class, args);
	}
	@Override
	public void run(String... args){
		if(args.length != 1){
			System.out.println("Usage: java -jar github-activity-cli.jar <github-username>");
			return;
		}
		String username = args[0];
		System.out.println("Fetching recent GitHub activity for: "+ username);
		String json = fetchGitHubEvents(username);
		if(json != null){
			parseAndDisplayEvents(json);
		}
	}
	private  String fetchGitHubEvents(String username){
		String url = "https://api.github.com/users/" + username +"/events";
		try{
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
			int status = connection.getResponseCode();
			if(status == 404){
				System.out.println("Error: GitHub user not found.");
				return null;
			}
			else if(status == 403){
				System.out.println("Error: API rate limit exceeded. Try again later.");
				return null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null){
				response.append(line);
			}
			reader.close();
			connection.disconnect();
			return response.toString();
		} catch (Exception e){
			System.out.println("Error fetching data: "+ e.getMessage());
			return null;
		}
	}
	private void parseAndDisplayEvents(String json){
		try{
			ObjectMapper mapper = new ObjectMapper();
			JsonNode events = mapper.readTree(json);
			int count = 0;
			if(events == null || !events.isArray() || events.size() == 0){
				System.out.println("No recent public activity found.");
				return;
			}
			System.out.println("========== Recent Activity ===========");
			for(JsonNode event: events){
				String type = event.get("type").asText();
				String repo = event.get("repo").path("name").asText();
				String message;
				switch (type){
					case "PushEvent" -> {
						int commits = event.get("payload").get("commits").size();
						message = "🟢 Pushed " + commits +" commit(s) to "+ repo;
					}
					case "IssueEvent" -> {
						String action = event.get("payload").get("action").asText();
						message = "‼️"+action.substring(0, 1).toUpperCase() + action.substring(1) + "as issue in " + repo;
					}
					case "WatchEvent" -> message = "⭐️ Starred " + repo;
					case "ForkedEvent" -> message = "🍽️ Forked"+ repo;
					case "PullRequestEvent" -> {
						String action = event.get("payload").get("action").asText();
						message = "📦 "+action.substring(0, 1).toUpperCase() + action.substring(1) + "a pull request in " + repo;
					}
					default -> message = "🧀 Did " + type + "in" + repo;
				}
				System.out.println("- " + message);
				if(++count == 10) break;
			}
			System.out.println("======================================");
		} catch (Exception e){
			System.out.println("Error parsing JSON: " + e.getMessage());
		}
	}
}
