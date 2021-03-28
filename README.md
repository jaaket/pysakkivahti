# Pysäkkivahti

A web application for displaying the schedule for a public transportation stop and line.

The application uses a GTFS (https://developers.google.com/transit/gtfs/) feed file to acquire schedule information.
For Helsingin Seudun Liikenne, you can download the file from https://infopalvelut.storage.hsldev.com/gtfs/hsl.zip.
The location of the file needs to be provided as an environment variable.

## Dependencies

* JDK 11

## Environment variables

| Variable name | Description |
| ------------- | ----------- |
| GTFS_ZIP_PATH | Location of the GTFS feed file

## Build and run a release

```
./mwnv package
```

Run the resulting JAR: 
```
GTFS_ZIP_PATH=/home/example/hsl.zip java -jar target/pysakkivahti-VERSION.jar
```

## TODO

* Inputs for stop and route ids
* Search stops
* Search routes
* Display realtime info about vehicles on specified route
* Use a real database to load GTFS data into to speed up startup