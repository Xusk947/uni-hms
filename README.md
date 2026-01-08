# HMS - Hospital Management System

## How to Run

### Option 1: Command Line

```bash
# Compile
javac -d out -sourcepath src src/Main.java

# Run
java -cp out Main
```

### Option 2: IntelliJ IDEA

1. Open the project in IntelliJ IDEA
2. Right-click on `src/Main.java`
3. Select **Run 'Main.main()'**

## Login

Use any Staff ID from `clinicians.csv` or `staff.csv` to login (e.g., `C001`).

## Features

- Nothing, its Uni project. Wish me luck pleaseeee

## Project Structure

Everything here is stored in `src` folder and you can start to check it from `Main.java`

Each folder has its own thing to do

For example

Controllers, Services and Views are the part of MVC pattern logic while utils/parser and models on other hand are the part of business logic

Also folder `views` has a GUI components in folder `components` and all app pages in folder `pages`, few forms just to create nice visuals for modals you will see while editing some part of appointmenets for example

