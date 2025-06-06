
# 🕹️ Chat GPT Swing Agent

A Java 1.8 Agent that attaches to any Swing-based Java application to capture user interactions (clicks, text input, table selections, dropdown choices, etc.) and enables automation through REST APIs.

## 🚀 Features

- 🎯 Attach as a Java Agent to any Swing application (`Application B`).
- 🔍 Captures user interactions:
  - Component IDs, parent windows, titles.
  - Inputs like text, selected dropdown items, table row data, etc.
- 🔗 Tracks components across multiple frames, dialogs, or windows.
- 🗺️ Saves user actions in:
  - A JSON file.
  - SQLite database.
- 🔧 REST API endpoints:
  - `/start-record` — Starts capturing user events.
  - `/stop-record` — Stops capturing and saves data.
  - `/automation` — Reads recorded JSON and replays the actions as automation.

## 🧠 Stack

- Java 1.8
- Swing
- SQLite
- Gson
- Maven
- Java Agent API
- Simple Java HTTP REST server

## 📦 Project Structure

```
chat_gpt_swing/
├── agent/               → Java Agent Maven Project
├── example-app/         → Example Swing Application for Testing
├── deploy/              → Deployment folder with JNLP, JARs, and server
│   └── server/          → Maven Project: Simple HTTP File Server
└── scripts/             → Bash and Windows deployment scripts
```

## ⚙️ Build Instructions

### 🔧 Build the Agent and Example App:

```bash
cd agent
mvn clean package

cd ../example-app
mvn clean package
```

### 🌐 Build the HTTP Server:

```bash
cd ../deploy/server
mvn clean package
```

## 🚀 Running the HTTP Server

### Run the HTTP server to serve the JNLP:

```bash
cd deploy/server
java -jar target/simple-http-server-1.0-SNAPSHOT-jar-with-dependencies.jar 8080 ..
```

- Access JNLP:
```
http://localhost:8080/agent-swing-recorder.jnlp
```

## ☕ Running via Java Web Start

1. Open the URL to the JNLP file in your browser:
```
http://localhost:8080/agent-swing-recorder.jnlp
```
2. Java Web Start will launch the agent.

## 🔥 REST API

- **Start Recording:**
```
POST http://localhost:4567/start-record
```

- **Stop Recording:**
```
POST http://localhost:4567/stop-record
```
→ Saves to JSON and SQLite.

- **Automation (Replay):**
```
POST http://localhost:4567/automation
```
→ Send the recorded JSON file to replay actions.

## 📜 Example JSON Action Format

```json
{
  "componentType": "JTable",
  "componentId": "table123",
  "parentId": "frameMain",
  "parentTitle": "Main Window",
  "inputValue": "Row 1, Column 2",
  "timeToNext": 500
}
```

## 💻 Deployment Scripts

- `deploy/run_server.sh` → Bash script to start HTTP server.
- `deploy/run_server.bat` → Windows script to start HTTP server.

## 🛠️ Requirements

- Java 1.8+
- Maven
- SQLite (embedded)
- JNLP support (Java Web Start)

## 🔗 Repository

[GitHub Repo](https://github.com/akumosstl/chat_gpt_swing.git)

## 🧠 Author

**Alisson Pedrina**

## 📃 License

MIT License (or specify your preferred license)

## 🚀 Next Steps

- ✅ Add more Swing component types.
- ✅ Improve component ID detection.
- 🚀 Add support for more complex user workflows.

## ❤️ Contributing

Pull requests are welcome. Open an issue for feature requests, questions, or bugs.
