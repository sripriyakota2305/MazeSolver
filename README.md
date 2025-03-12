# MazeSolver
🧩 Dynamic Maze Solver
📌 Overview
Dynamic Maze Solver is an interactive Java application built using Java Swing to visualize and solve mazes using the Breadth-First Search (BFS) algorithm. The application allows users to:
✅ Set Start & End Points dynamically using mouse clicks
✅ Visualize the Shortest Path using BFS algorithm
✅ Adjust the Grid Size Dynamically for different screen sizes
✅ Detect No Solution Cases with error handling

The graphical interface makes it an educational tool for understanding graph traversal and pathfinding algorithms.

🛠️ Technologies Used
Java Swing → GUI development
Breadth-First Search (BFS) → Pathfinding algorithm
Event Listeners (Mouse & Keyboard) → Interactive controls
Dynamic Resizing → Auto-adjust grid based on window size
📂 Project Structure
bash
Copy
Edit
Maze-Solver/
│── src/
│   ├── DynamicMazeSolver.java  # Main Java program
│── assets/
│   ├── screenshots/            # Output screenshots
│── README.md                   # Project documentation
│── LICENSE                     # (Optional) License file
🚀 Features
✔️ Interactive GUI – Set start and end points dynamically using mouse clicks
✔️ Shortest Path Highlighting – Uses BFS to mark the optimal path
✔️ Resizable Grid – Adapts to different window sizes
✔️ Wall & Path Differentiation – Clearly marked obstacles and pathways
✔️ Error Handling – Displays a message if no path is found

🔍 Code Example (Maze Solver in Java)
java
Copy
Edit
private void solveMaze() {
    if (startX == -1 || startY == -1 || endX == -1 || endY == -1) {
        JOptionPane.showMessageDialog(this, "Please set start and end points.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Queue<Point> queue = new LinkedList<>();
    boolean[][] visited = new boolean[rows][cols];
    Map<Point, Point> parentMap = new HashMap<>();

    queue.offer(new Point(startX, startY));
    visited[startX][startY] = true;

    while (!queue.isEmpty()) {
        Point current = queue.poll();
        int x = current.x;
        int y = current.y;

        if (x == endX && y == endY) {
            markShortestPath(parentMap, current);
            repaint();
            return;
        }

        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];

            if (isValid(newX, newY) && maze[newX][newY] == 0 && !visited[newX][newY]) {
                queue.offer(new Point(newX, newY));
                visited[newX][newY] = true;
                parentMap.put(new Point(newX, newY), current);
            }
        }
    }

    JOptionPane.showMessageDialog(this, "No path found.", "Error", JOptionPane.ERROR_MESSAGE);
}
🖥️ How to Run the Project
1️⃣ Install Java Development Kit (JDK) (Download Here)
2️⃣ Compile the Java Program

sh
Copy
Edit
javac DynamicMazeSolver.java
3️⃣ Run the Program

sh
Copy
Edit
java DynamicMazeSolver
4️⃣ Interact with the Maze

Left-Click → Set the Start Point
Right-Click → Set the End Point
Press Enter → Solve the maze using BFS Algorithm

