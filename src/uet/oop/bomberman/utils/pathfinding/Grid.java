package uet.oop.bomberman.utils.pathfinding;

import java.util.ArrayList;
import java.util.List;

/**
 * The grid of nodes we use to find path
 */
public class Grid {
    public Node[][] nodes;
    int gridWidth, gridHeight;

    /**
     * Create a new Grid with tile prices.
     *
     * @param width      Grid width
     * @param height     Grid height
     * @param tile_costs 2d array of floats, representing the cost of every tile.
     *                   0 means walkable tile.
     *                   1 means unwakable tile.
     */
    public Grid(int width, int height, int[][] tile_costs) {
        gridWidth = width;
        gridHeight = height;
        nodes = new Node[height][width];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                nodes[y][x] = new Node(x, y, tile_costs[y][x] > 0 ? 1 : 0);
    }

    /**
     * Create a new grid of just walkable / unwalkable tiles.
     *
     * @param width         Grid width
     * @param height        Grid height
     * @param walkableTiles the tilemap. true for walkable, false for blocking.
     */
    public Grid(int width, int height, boolean[][] walkableTiles) {
        gridWidth = width;
        gridHeight = height;
        nodes = new Node[height][width];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                nodes[y][x] = new Node(x, y, walkableTiles[y][x] ? 0 : 1);
    }

    public List<Node> get8Neighbours(Node node) {
        List<Node> neighbours = new ArrayList<Node>();

        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) continue;

                int checkX = node.x + x;
                int checkY = node.y + y;

                if (checkX >= 0 && checkX < gridWidth && checkY >= 0 && checkY < gridHeight) {
                    neighbours.add(nodes[checkX][checkY]);
                }
            }

        return neighbours;
    }

    public List<Node> get4Neighbours(Node node) {
        List<Node> neighbours = new ArrayList<Node>();

        if (node.y + 1 >= 0 && node.y + 1  < gridHeight) neighbours.add(nodes[node.y + 1][node.x]); // N
        if (node.y - 1 >= 0 && node.y - 1  < gridHeight) neighbours.add(nodes[node.y - 1][node.x]); // S
        if (node.x + 1 >= 0 && node.x + 1  < gridWidth) neighbours.add(nodes[node.y][node.x + 1]); // E
        if (node.x - 1 >= 0 && node.x - 1  < gridWidth) neighbours.add(nodes[node.y][node.x -1]); // W

        return neighbours;
    }
}