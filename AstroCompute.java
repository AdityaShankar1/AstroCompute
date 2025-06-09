import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;


//Welcome to this project's source code!
public class AstroCompute {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
System.out.println("AstroCompute: Smart Astronomical Scheduler");
	//Step-1: I'm going to create some sample events
	
	CelestialEvent[] events = generateSampleEvents();
	//Step-2: Print the events 
	System.out.println("\n🔍 Best Non-Overlapping Schedule (Sorted by Score):");
	List<ScoredEvent> bestSchedule = getOptimalSchedule(events);
	// Print generation time stamp in terminal 
	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	System.out.println("\n📅 Generated on: " + now.format(formatter));

	for (ScoredEvent se : bestSchedule) {
	    System.out.printf("%s → Score: %.3f%n", se.event, se.score);
	}
	writeScheduleToFile(bestSchedule, "astro_schedule.txt");
	}
//CelestialEvent Class (Inner Class) 
static class CelestialEvent {
	String name;
	int startHour;
	int endHour; 
	double cloudCoverage; //% from 0.0 to 1.0
	double magnitude; //lower = brighter
	int priority; //1 to 5
	
CelestialEvent(String name, int startHour, int endHour, double cloudCoverage, double Magnitude, int priority){
//To prevent illegal inputs and buffer overflows:
if(magnitude<0) {
	throw new IllegalArgumentException("magnitude must be positive!");
}
if(cloudCoverage < 0 || cloudCoverage > 1) {
	throw new IllegalArgumentException("CloudCoverage must lie between 0 and 1");
}
if(priority<1 || priority > 5) {
	throw new IllegalArgumentException("Priority must lie between 1 and 5");
}
this.name=name;
this.startHour=startHour;
this.endHour=endHour;
this.cloudCoverage=cloudCoverage;
this.magnitude=magnitude;
this.priority=priority;
}
public String toString() { return String.format("%s (%02d:00-%02d:00), Cloud: %.0f%%, Mag: %.3f, Priority:%d", name, startHour, endHour, cloudCoverage * 100, magnitude, priority);
}
}

//Wrapper class 
static class ScoredEvent{
	CelestialEvent event;
	double score;
	ScoredEvent(CelestialEvent event, double score){
		this.event = event;
		this.score = score; 
	}
}

//Generate sample events
static CelestialEvent[] generateSampleEvents() {
    Random rand = new Random(); //Randomizing to mimic real time data
    String[] names = {
        "Meteor Shower", "Lunar Eclipse", "Planet Transit", "Comet Sighting",
        "Supernova Watch", "Aurora Borealis", "Occultation", "Nebula Scan",
        "Deep Sky Survey", "Solar Flare Monitor"
    };

    CelestialEvent[] sample = new CelestialEvent[names.length];

    for (int i = 0; i < names.length; i++) {
        int start = rand.nextInt(24);            // 0 to 23
        int end = Math.min(start + rand.nextInt(3) + 1, 24); // 1 to 3 hrs later, capped at 24
        double clouds = rand.nextDouble();        // 0.0 to 1.0
        double magnitude = 0.5 + rand.nextDouble() * 3.0; // 0.5 to 3.5 (realistic)
        int priority = 1 + rand.nextInt(5);       // 1 to 5

        sample[i] = new CelestialEvent(names[i], start, end, clouds, magnitude, priority);
    }

    return sample;
}

static double predictVisibility(CelestialEvent e) {double w1=0.4; double w2=0.3; double w3=0.3; 
double cloudScore=1-e.cloudCoverage;
double brightnessScore = (e.magnitude <=0.01)?0:1.0/e.magnitude; //To prevent division by 0 error
double priorityScore=e.priority/5.0; //To normalize priority from 0-5 to 0-1
return w1*cloudScore + w2*brightnessScore + w3*priorityScore; 
}
//sort events by visibility and select best non-overlapping ones
static List<ScoredEvent> getOptimalSchedule(CelestialEvent[] events) {
    List<ScoredEvent> scoredList = new ArrayList<>();

    // Step 1: Score each event
    for (CelestialEvent e : events) {
        double score = predictVisibility(e);
        scoredList.add(new ScoredEvent(e, score));
    }

    // Step 2: Sort by score (highest first)
    scoredList.sort((a, b) -> Double.compare(b.score, a.score));

    // Step 3: Greedy selection
    List<ScoredEvent> selected = new ArrayList<>();
    int lastEnd = -1;

    for (ScoredEvent se : scoredList) {
        if (se.event.startHour >= lastEnd) {
            selected.add(se);
            lastEnd = se.event.endHour;
        }
    }
    return selected;
}
static void writeScheduleToFile(List<ScoredEvent> schedule, String filename) {
    try (FileWriter writer = new FileWriter(filename)) {

        // ✅ Get current timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // ✅ Write timestamp line
        writer.write("🔭 AstroCompute Optimal Observation Schedule\n");
        writer.write("Generated on: " + now.format(formatter) + "\n\n");

        for (ScoredEvent se : schedule) {
            writer.write(String.format("%s → Score: %.3f%n", se.event, se.score));
        }

        writer.write("\n✨ Total Events Selected: " + schedule.size());
        System.out.println("✅ Schedule written to: " + filename);
    } catch (IOException e) {
        System.out.println("❌ Error writing to file: " + e.getMessage());
    }
}

}