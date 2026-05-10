# Air Traffic Control Simulation with Interactive Map

An academic Java project developed as part of the Object-Oriented Programming 2 course.

The application simulates aircraft movement between airports on an interactive map. The main focus of the project is object-oriented design and basic concurrent programming, where aircraft are animated using separate threads.

## Features

- Interactive map showing airports and aircraft
- Aircraft movement between selected departure and arrival airports
- Each aircraft is executed in a separate thread
- Airports displayed as squares on the map
- Aircraft displayed as moving circles
- Airport blinking state changes on user click
- Manual input forms for airport and flight data
- Input validation for entered data
- Reading airport and flight data from CSV files
- Writing and reading data from files
- Inactivity timer with a `Stay Active` dialog
- Object-oriented application structure

## Technologies Used

- Java
- Object-Oriented Programming
- Multithreading
- File I/O
- CSV data handling

## Project Structure

```text
src/                    Java source files
projectCsvResources/     CSV files used for loading example data
out/                    Compiled files, generated after compilation
```
## How to Run

Make sure Java is installed on your computer.

From the project root directory, compile all source files using PowerShell:
```text
$files = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }
javac -d out $files
```

Run the application:
```text
java -cp out tests.mainProgram
```
The out folder is generated during compilation and should not be committed to the repository.

## Usage

After starting the application, airport and flight data should be entered before starting the simulation.

Data can be entered manually using the following buttons:

1) Enter airport information

2) Enter flight information

**Airport data should be entered before flight data!**

The most practical way to test the application is by loading the provided CSV files.

To load example airport data:

1) Click **Load from file**
2) Select the file by entering its path:
```text
projectCsvResources/data.csv
```
3) Click **LOAD AIRPORTS**

To load example flight data:

4) Click **Load from file**

5) Select the file by entering its path:
```text
projectCsvResources/data1.csv
```
6) Click **LOAD FLIGHTS**

7) After loading the data, click:

**Show airports map**

This opens the interactive map.

On the map:

- **Select All** displays all entered airports.

- **Start simulation** starts the aircraft movement simulation.

Aircraft are displayed as moving circles.

Airports are displayed as squares.

Clicking on an airport changes its blinking state.

## Inactivity Timer
The application includes an inactivity timer. If the user is inactive for a certain period of time, a dialog appears with the option to continue using the application by clicking:

**Stay Active**

## Notes
The project was developed for academic purposes and focuses on practicing Java, object-oriented programming, file handling, GUI interaction, and basic multithreading.
