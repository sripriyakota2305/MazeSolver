import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class DynamicMazeSolver extends JFrame {
    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color PATH_COLOR = Color.GREEN;
    private static final Color EMPTY_COLOR = Color.WHITE;
    private static final Color VISITED_COLOR = Color.CYAN;
    private static final Color START_COLOR = Color.ORANGE;
    private static final Color END_COLOR = Color.BLUE;
    private static final Color SHORTEST_PATH_COLOR = Color.PINK;

    private int[][] maze;
    private int rows;
    private int cols;
    private int startX = -1;
    private int startY = -1;
    private int endX = -1;
    private int endY = -1;

    public DynamicMazeSolver(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new int[rows][cols];
        setWalls(); // Set static walls

        setTitle("Dynamic Maze Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLayout(new BorderLayout());

        MazePanel mazePanel = new MazePanel();
        mazePanel.addMouseListener(new MazeMouseListener());
        add(mazePanel, BorderLayout.CENTER);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    solveMaze();
                }
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void setWalls() {
        // Example of static wall setup
        for (int i = 0; i < rows; i++) {
            if (i % 2 == 0) {
                maze[i][3] = 1; // vertical wall
                maze[i][7] = 1; // vertical wall
            }
        }
        for (int j = 0; j < cols; j++) {
            if (j % 2 == 0) {
                maze[3][j] = 1; // horizontal wall
                maze[7][j] = 1; // horizontal wall
            }
        }
    }

    class MazePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();
            int cellSize = Math.min(width / cols, height / rows);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Color color = getColorForCell(i, j);
                    g.setColor(color);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    class MazeMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int width = getWidth();
            int height = getHeight();
            int cellSize = Math.min(width / cols, height / rows);

            int y = e.getX() / cellSize;
            int x = e.getY() / cellSize;

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (isValid(x, y) && maze[x][y] == 0 && startX == -1 && startY == -1) {
                    startX = x;
                    startY = y;
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if (isValid(x, y) && maze[x][y] == 0 && endX == -1 && endY == -1) {
                    endX = x;
                    endY = y;
                }
            }

            repaint();
        }
    }

    private void solveMaze() {
        if (startX == -1 || startY == -1 || endX == -1 || endY == -1) {
            JOptionPane.showMessageDialog(this, "Please set start and end points.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.List<Point> shortestPath = findShortestPath();
        if (shortestPath != null) {
            markShortestPath(shortestPath);
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No path found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private java.util.List<Point> findShortestPath() {
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
                java.util.List<Point> shortestPath = new ArrayList<>();
                Point node = new Point(endX, endY);
                while (!node.equals(new Point(startX, startY))) {
                    shortestPath.add(node);
                    node = parentMap.get(node);
                }
                shortestPath.add(new Point(startX, startY));
                Collections.reverse(shortestPath);
                return shortestPath;
            }

            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (isValid(newX, newY) && maze[newX][newY] == 0 && !visited[newX][newY]) {
                    queue.offer(new Point(newX, newY));
                    visited[newX][newY] = true;
                    parentMap.put(new Point(newX, newY), new Point(x, y));
                }
            }
        }

        return null; // No path found
    }

    private void markShortestPath(java.util.List<Point> shortestPath) {
        for (Point p : shortestPath) {
            maze[p.x][p.y] = 4; // Mark the shortest path
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    private Color getColorForCell(int x, int y) {
        if (maze[x][y] == 1) {
            return WALL_COLOR;
        } else if (maze[x][y] == 2) {
            return PATH_COLOR;
        } else if (maze[x][y] == 3) {
            return VISITED_COLOR;
        } else if (x == startX && y == startY) {
            return START_COLOR;
        } else if (x == endX && y == endY) {
            return END_COLOR;
        } else if (maze[x][y] == 4) {
            return SHORTEST_PATH_COLOR;
        } else {
            return EMPTY_COLOR;
        }
    }

    public static void main(String [] args) {
        int rows = 20; // Increase number of rows
        int cols = 20; // Increase number of columns

        SwingUtilities.invokeLater(() -> new DynamicMazeSolver(rows, cols));
    }

    private static final int[] dx = {0, 1, 0, -1};
    private static final int[] dy = {1, 0, -1, 0};
}

