# Greenhouse Real-Time System

A Java desktop application for monitoring and controlling a simulated greenhouse in real time. The system models greenhouse sensors and actuators, applies crop-specific settings, and presents live readings through a Swing-based graphical interface.

Repository: [CodeWithMoaz/Greenhouse-Real-Time-System](https://github.com/CodeWithMoaz/Greenhouse-Real-Time-System)

## Features

- Monitor temperature, humidity, pH, light intensity, and water level.
- Control greenhouse actuators including HVAC, humidification, lighting, water flow, nutrient dispensing, and alerts.
- Select a crop and apply its configured operating ranges.
- View live sensor dashboards and a camera view.
- Process sensor readings and power events with Esper EPL statements.
- Start and stop the simulated greenhouse system from the desktop UI.

## Technology

- Java 21
- Java Swing
- Apache Esper 5.3.0 for event processing
- Apache Ant and Apache NetBeans project structure
- Log4j and SLF4J for logging

## Requirements

- JDK 21 or newer
- Apache NetBeans with Java SE support
- Apache Ant, if building from the command line

## Getting Started

### Run with NetBeans

1. Clone the repository:

   ```bash
   git clone https://github.com/CodeWithMoaz/Greenhouse-Real-Time-System.git
   cd Greenhouse-Real-Time-System
   ```

2. Open the project in NetBeans using **File > Open Project**.
3. Confirm that the project uses JDK 21.
4. Run the project. The application entry point is `esper.Main`.

The required Esper and logging dependencies are included in the `lib/` directory. NetBeans may also prompt you to configure its bundled `AbsoluteLayout` library for the generated Swing forms.

### Build with Ant

From the project root:

```bash
ant clean jar
```

To run the application through the Ant project:

```bash
ant run
```

The generated JAR is placed in `dist/Greenhouse_Project.jar`.

## Project Structure

```text
src/esper/   Esper configuration and application entry point
src/events/  Event objects emitted by the simulated sensors
src/model/   Sensors, actuators, crop settings, and controller logic
src/view/    Swing dashboards and NetBeans GUI forms
lib/         Runtime libraries
nbproject/   NetBeans project configuration
test/        Test sources
```

## Architecture Diagrams

The repository also contains class, use-case, sequence, and state-machine diagrams documenting the system design.

## Notes

This project simulates greenhouse hardware in software. Sensor and actuator behavior is implemented by Java classes and background threads rather than connected physical devices.

## Author

[CodeWithMoaz](https://github.com/CodeWithMoaz)
