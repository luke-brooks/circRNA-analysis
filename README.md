# circRNA-analysis

## Running the BSJ Analyzer
1. To run via VS Code Java Debugger, ensure that the `launch.json` file has the configuration arguments `"vmArgs" : "-Xms6g -Xmx8g"` added to the project run definition. These arguments are needed to expand the Java Heap Size to hold the Chromosome references (extremely large text files) in memory while the app runs.

### The Java Debugging tool for VS Code is straight garbage
It never imports the projects correctly and gets stuck trying to do that when you click the `Run/Debug` button. To get around the annoying & busted add-on, run the app manually using the terminal command below:

```bash
cd /Users/dremelse/Documents/ZiegelbauerLab/Bioinformatics/new-circ-rna ; /usr/bin/env /Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/bin/java -agentlib:jdwp=transport=dt_socket,server=n,suspend=y,address=localhost:54412 -Xms6g -Xmx8g -XX:+ShowCodeDetailsInExceptionMessages -cp /Users/dremelse/Documents/ZiegelbauerLab/Bioinformatics/new-circ-rna/bin App 
```

### Example `launch.json` file in its entirety.

```
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch Current File",
            "request": "launch",
            "mainClass": "${file}"
        },
        {
            "type": "java",
            "name": "Launch App",
            "request": "launch",
            "mainClass": "App",
            "projectName": "circRNA-analysis_5f3f4194",
            "vmArgs" : "-Xms6g -Xmx8g"
        }
    ]
}
```