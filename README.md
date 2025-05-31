# GitHub Activity CLI

A simple command-line application built with Spring Boot that fetches and displays recent public activity for any GitHub user using the GitHub Events API.

---

## 🚀 Features

- ✅ Accepts GitHub username as a command-line argument
- ✅ Fetches up to 10 recent public events via the GitHub API
- ✅ Displays activity in a human-readable format (e.g. pushes, issues, stars, forks)
- ✅ Graceful error handling (invalid usernames, rate limits, etc.)

---

## 🔧 Technologies Used

- Java 17+
- Spring Boot
- Jackson (for JSON parsing)
- No external HTTP or API libraries used — pure Java `HttpURLConnection`

---

## 🧑‍💻 Usage

### 1. Build the application

```bash
./mvnw clean package
```
### 2. Run the CLI
```bash
java -jar target/github-activity-cli-0.0.1-SNAPSHOT.jar <github-username>
```
### 3.Output Example
```bash
Fetching recent GitHub activity for: kamranahmedse

====== Recent Activity ======
🟢 Pushed 2 commit(s) to kamranahmedse/developer-roadmap
⭐ Starred kamranahmedse/developer-roadmap
❗ Opened an issue in kamranahmedse/roadmap.sh
🍴 Forked kamranahmedse/terminalizer
=============================
```
### ❗ Notes
- Uses GitHub’s public API:
https://api.github.com/users/<username>/events
- Subject to GitHub’s rate limits for unauthenticated requests.
###  📜 License

This project is licensed under the MIT License. Feel free to use and modify.

### 🌐 Project URL

[GitHub User Activity on roadmap.sh](https://roadmap.sh/projects/github-user-activity)