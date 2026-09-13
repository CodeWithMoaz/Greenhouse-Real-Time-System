# Greenhouse Real-Time System

A Java desktop application for monitoring and controlling a simulated greenhouse in real time. The system models greenhouse sensors and actuators, applies crop-specific settings, and presents live readings through a Swing-based graphical interface.

<img width="1263" height="712" alt="{8EBA1AAB-4780-4C09-A49C-15ED60C01A17}" src="https://github.com/user-attachments/assets/21e8cb52-d5b5-4ed8-b7ac-1a514f65ad89" />


Repository: [CodeWithMoaz/Greenhouse-Real-Time-System](https://github.com/CodeWithMoaz/Greenhouse-Real-Time-System)

## Features

- Monitor temperature, humidity, pH, light intensity, and water level.
- Control greenhouse actuators including HVAC, humidification, lighting, water flow, nutrient dispensing, and alerts.
- Select a crop and apply its configured operating ranges.
<img width="1258" height="708" alt="{C5E78C45-2D96-4E9A-B7CD-B9D8521953D5}" src="https://github.com/user-attachments/assets/e78f976f-6a44-4be6-b53e-a47bf4eeb7a4" />

- View live sensor dashboards and a camera view.
<img width="1312" height="727" alt="{35747D04-51A6-441D-8621-0BE8B9B721A5}" src="https://github.com/user-attachments/assets/8b5dd789-cf2a-4848-95e2-f1681f51300d" />
<img width="1312" height="728" alt="{211E92A2-D9D6-4373-9A31-0B019CE35D6E}" src="https://github.com/user-attachments/assets/0532df02-5292-4ed6-960c-3d6aa7fe3ee1" />
<img width="1267" height="732" alt="{F0525E9D-27CB-4907-B933-8BE640EB2E3B}" src="https://github.com/user-attachments/assets/125bae67-ff4f-4f33-b7ba-6e7d5d5f41e9" />
<img width="1257" height="729" alt="{30E38E8E-000C-4182-B442-C4BF227FB9E6}" src="https://github.com/user-attachments/assets/9813ff09-480c-411e-abc4-7685c955bd35" />

- Process sensor readings and power events with Esper EPL statements.
  <img width="510" height="616" alt="{EA4C2CA5-E18C-4B3D-80D3-947790D25026}" src="https://github.com/user-attachments/assets/d9fc6d91-ef0f-43e9-8d6e-91b3aba9fe79" />

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
