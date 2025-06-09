# AstroCompute 🌌
A Core Java simulation engine that schedules astronomical observation events using a custom visibility scoring system and greedy algorithm logic.

## 🔍 Features
- Randomly generates realistic celestial events with cloud coverage, brightness (magnitude), and priority
- Scores each event using a machine-learning inspired formula (no ML libraries used)
- Greedy scheduling algorithm selects the best non-overlapping events
- Outputs results to both terminal and `.txt` file
- Fully implemented in a single `.java` file — no frameworks or dependencies

## 🚀 Tech Stack
- Java (Core, JDK 21)
- `java.util`: Random, List, ArrayList
- `java.io`: FileWriter
- `java.time`: LocalDateTime, DateTimeFormatter

## 📦 Output Sample

🔭 AstroCompute Optimal Observation Schedule
Generated on: 2025-06-09 18:15

Meteor Shower (21:00–23:00), Cloud: 10%, Mag: 1.20, Priority: 5 → Score: 0.958
Planet Transit (02:00–04:00), Cloud: 20%, Mag: 2.50, Priority: 3 → Score: 0.676

✨ Total Events Selected: 2

## 💡 Author
Aditya Shankar  
Built for interview preparation, DSA showcase, and ML logic practice using clean Core Java.