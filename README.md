# circRNA-analysis

## Running the BSJ Analyzer
1. To run via VS Code Java Debugger, ensure that the `launch.json` file has the configuration arguments `"vmArgs" : "-Xms6g -Xmx8g"` added to the project run definition. These arguments are needed to expand the Java Heap Size to hold the Chromosome references (extremely large text files) in memory while the app runs.

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