# csc413-tankgame

# Required Information about the Tank Game

## Version of Java Used: SDK 21

## IDE used: IntelliJ IDEA

## Steps to Import project into IDE:

1. Open Your Project:
Open IntelliJ IDEA and load the project.
3. Configure the Artifact:
Go to File > Project Structure (or press Ctrl+Alt+Shift+S).
In the Project Structure window, select the Artifacts option on the left.
Click the + button to add a new artifact, then select JAR > From
modules with dependencies.
Choose your main module(lanucher.java)
IntelliJ will automatically set up the output directory and include
necessary libraries in the JAR if required.
4. Build the Artifact:
Once the artifact is configured, click OK to close the Project Structure
window.
Now, go to Build > Build Artifact
Select the artifact you just created and click Build.
IntelliJ will compile your project and create the JAR file in the
specified output directory.
5. Locate the JAR File:
After the build is complete, navigate to the output directory to find
your JAR file. By default, this directory might be something like
out/artifacts/[artifact_name].
If you want you can run the launcher.java to run the game. But then you
will need to set up the resource folder in project structure > modules>
resources.

## Steps to Build your Project:

1. Prerequisites
    -  Tools needed (e.g., Java JDK, Git)
- SDK 21

2. Clone the Repository
    - Provide the git command to clone your project
    - Example: `git clone git@github.com:AnshajVats/tank-game.git`

3. Navigate to Project Directory
    - Instruct users to change to the project directory
    - Example: `cd tank-game`
4. Setup the SDK
   - Example: `export JAVA_HOME=/path/to/jdk-21`
   - On IntelliJ IDEA, go to File -> Project Structure -> Project SDK -> Add SDK -> Select the path to the SDK 21
## Steps to run your Project:

1. IntelliJ IDEA
   - Open the project in IntelliJ IDEA
   - Right-click on the `Launcher.java` file
   - Select `Run 'Launcher.main()'`
   - The game will start and you can play it.
2. jar file
   - Open the terminal
   - Navigate to the jar folder
   - Run the command `java -jar csc413-tankgame-AnshajVats.jar`
   - The game will start and you can play it.
   - If it didn't run then open IntelliJ IDEA 
   - go to jar folder 
   - then the jar file is there run it manually. (double click on it)
   - If it didn't run open IntelliJ IDEA and refer to step 1.

## Controls to play your Game:

|               | Player 1 | Player 2    |
|---------------|----------|-------------|
|  Forward      | w        | up_arrow    |
|  Backward     | s        | down_arrow  |
|  Rotate left  | a        | left_arrow  |
|  Rotate Right | d        | right_arrow |
|  Shoot        | spacebar | enter       |

<!-- you may add more controls if you need to. -->
