import java.io.IOException;
import java.util.ArrayList;

public class Room {
    int width;
    int height;
    String[][] roomGrid;
    int debug = 3;
    //0 = none | 1 = room validity & print | 2 = Object checks | 3 = Pathing visualization
    ArrayList<Integer> objectCoords = new ArrayList<>();
    ArrayList<Integer> objectSizes = new ArrayList<>();
    ArrayList<Integer> objectDirections = new ArrayList<>();



    public Room (int width, int height, String type) throws IOException{
        this.width = width;
        this.height = height;
        roomGrid = new String[height][width];
        createRoom(width, height);
        if (type.equals("bathroom")) {
            bathroom();
        }
    }



    public String[][] createRoom(int width, int height) {

        //Fill grid with -
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                roomGrid[i][j] = "-";
            }
        }

        //WALLS
        //top row
        for (int i = 0; i < width; i++) {
            roomGrid[0][i] = "X";
        }
        //bottom row
        for (int i = 0; i < width; i++) {
            roomGrid[height - 1][i] = "X";
        }
        //left col
        for (int i = 0; i < height; i++) {
            roomGrid[i][0] = "X";
        }
        //right col
        for (int i = 0; i < height; i++) {
            roomGrid[i][width - 1] = "X";
        }

        return roomGrid;
    }

    public void printRoom() throws IOException {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(roomGrid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public String[][] bathroom() throws IOException{
        int tries = 0;
        do {

            if (debug > 0) System.out.println();

            objectCoords = new ArrayList<>();
            objectSizes = new ArrayList<>();

            //Create empty room
            roomGrid = createRoom(roomGrid[0].length, roomGrid.length);

            //Door
            placeDoor();

            //Bathtub
            placeDoubleObj("B");

            //Toilet
            placeObj("T");

            //Sink
            placeObj("S");

            if (debug > 0) printRoom();

            tries++;

        } while (!checkRoom());

        if (debug > 0) System.out.println("Room valid!\nIt took " + tries + " tries/try.\n");
        return roomGrid;

    }

    public void placeObj(String letter) {
        int x = 0;
        int y = 0;
        while (!roomGrid[y][x].equals("-")) {
            x = (int) (Math.random() * (roomGrid[0].length - 2)) + 1;
            y = (int) (Math.random() * (roomGrid.length - 2)) + 1;
        }
        objectCoords.add(x);
        objectCoords.add(y);
        objectSizes.add(1);
        roomGrid[y][x] = letter;
    }

    public void placeDoubleObj(String letter) {

        //First block
        int x = 0;
        int y = 0;
        while (!roomGrid[y][x].equals("-")) {
            x = (int) (Math.random() * (roomGrid[0].length - 2)) + 1;
            y = (int) (Math.random() * (roomGrid.length - 2)) + 1;
        }
        objectCoords.add(x);
        objectCoords.add(y);
        objectSizes.add(2);
        roomGrid[y][x] = letter;

        //Second block
        int x2 = 0;
        int y2 = 0;
        while (!roomGrid[y2][x2].equals("-")) {
            int rand = (int) (Math.random() * 4);
            if (rand == 0) { //Right
                x2 = x + 1;
                y2 = y;
            }
            if (rand == 1) { //Left
                x2 = x - 1;
                y2 = y;
            }
            if (rand == 2) { //Up
                x2 = x;
                y2 = y + 1;
            }
            if (rand == 3) { //Down
                x2 = x;
                y2 = y - 1;
            }
        }
        objectCoords.add(x2);
        objectCoords.add(y2);
        roomGrid[y2][x2] = letter;
    }

    public Boolean checkArea(int x, int y) {
        return (roomGrid[y + 1][x].equals("-") || roomGrid[y][x + 1].equals("-") || roomGrid[y - 1][x].equals("-") || roomGrid[y][x - 1].equals("-"))
                && !(roomGrid[y + 1][x].equals("=") || roomGrid[y][x + 1].equals("=") || roomGrid[y - 1][x].equals("=") || roomGrid[y][x - 1].equals("="));
    }

    public Boolean checkDoubleArea(int x, int y, int x2, int y2) {
        ArrayList freeSpace = new ArrayList();

        //Block 1
        if (roomGrid[y][x + 1].equals("-")) {
            freeSpace.add(0);
        }
        if (roomGrid[y][x - 1].equals("-")) {
            freeSpace.add(1);
        }
        if (roomGrid[y - 1][x].equals("-")) {
            freeSpace.add(2);
        }
        if (roomGrid[y + 1][x].equals("-")) {
            freeSpace.add(3);
        }
        if (roomGrid[y + 1][x].equals("=") || roomGrid[y][x + 1].equals("=") || roomGrid[y - 1][x].equals("=") || roomGrid[y][x - 1].equals("="))
            return false;

        //Block 2
        if (roomGrid[y2][x2 + 1].equals("-")) {
            freeSpace.add(0);
        }
        if (roomGrid[y2][x2 - 1].equals("-")) {
            freeSpace.add(1);
        }
        if (roomGrid[y2 - 1][x2].equals("-")) {
            freeSpace.add(2);
        }
        if (roomGrid[y2 + 1][x2].equals("-")) {
            freeSpace.add(3);
        }
        if (roomGrid[y2 + 1][x2].equals("=") || roomGrid[y2][x2 + 1].equals("=") || roomGrid[y2 - 1][x2].equals("=") || roomGrid[y2][x2 - 1].equals("="))
            return false;
        //0 = right, 1 = left, 2 = up, 3 = down

        int right = 0;
        int left = 0;
        int up = 0;
        int down = 0;
        for (int i = 0; i < freeSpace.size(); i++) {
            if (freeSpace.get(i).equals(0)) {
                right++;
            } else if (freeSpace.get(i).equals(1)) {
                left++;
            } else if (freeSpace.get(i).equals(2)) {
                up++;
            } else {
                down++;
            }
        }
        return (right > 1 || left > 1 || up > 1 || down > 1);
    }

    public int placeDoor() {
        //0 = right, 1 = left, 2 = up, 3 = down
        int side = (int) (Math.random() * 4);
        objectCoords.add(side);
        int rand;

        if (side == 0 || side == 1) {
            rand = (int) (Math.random() * (roomGrid.length - 2)) + 1;
            if (side == 0) {
                roomGrid[rand][roomGrid[0].length - 1] = "=";
                objectCoords.add(roomGrid[0].length - 1);
                objectCoords.add(rand);
            } else {
                roomGrid[rand][0] = "=";
                objectCoords.add(0);
                objectCoords.add(rand);
            }
        } else {
            rand = (int) (Math.random() * (roomGrid[0].length - 2)) + 1;
            if (side == 2) {
                roomGrid[0][rand] = "=";
                objectCoords.add(rand);
                objectCoords.add(0);
            } else {
                roomGrid[roomGrid[0].length - 1][rand] = "=";
                objectCoords.add(rand);
                objectCoords.add(roomGrid[0].length - 1);
            }
        }

        return side;
    }

    public Boolean checkRoom() throws IOException{
        if (debug > 0) System.out.println("Checking room...");
        int cursor = 3; // 0 = door side, 1 = door x, 2 = door y
        for (int i = 0; i < objectSizes.size(); i++) {

            //Single object
            if (objectSizes.get(i) == 1) {
                if (debug > 1) System.out.println("Checking " + objectCoords.get(cursor) + ", " + objectCoords.get(cursor + 1));

                //Area
                if (checkArea(objectCoords.get(cursor), objectCoords.get(cursor + 1))) {
                    if (debug > 1) System.out.println("✔ Area");
                } else {
                    if (debug > 1) System.out.println("✗ Area");
                    if (debug > 0) System.out.println("Room invalid");
                    return false;
                }

                //Pathing
                if (pathTo(objectCoords.get(cursor), objectCoords.get(cursor + 1))) {
                    if (debug > 1) System.out.println("✔ Pathing");
                } else {
                    if (debug > 1) System.out.println("✗ Pathing");
                    return false;
                }
                cursor += 2;
            }

            //Double object
            if (objectSizes.get(i) == 2) {

                if (debug > 1)
                    System.out.println("Checking " + objectCoords.get(cursor) + ", " + objectCoords.get(cursor + 1) + " and " + objectCoords.get(cursor + 2) + ", " + objectCoords.get(cursor + 3));

                //Area
                if (checkDoubleArea(objectCoords.get(cursor), objectCoords.get(cursor + 1), objectCoords.get(cursor + 2), objectCoords.get(cursor + 3))) {
                    if (debug > 1) System.out.println("✔ Area");
                } else {
                    if (debug > 1) System.out.println("✗ Area");
                    if (debug > 0) System.out.println("Room invalid");
                    return false;
                }

                //Pathing block 1
                if (pathTo(objectCoords.get(cursor), objectCoords.get(cursor + 1))) {
                    if (debug > 1)
                        System.out.println("✔ Pathing " + objectCoords.get(cursor) + ", " + objectCoords.get(cursor + 1));
                } else {
                    if (debug > 1)
                        System.out.println("✗ Pathing " + objectCoords.get(cursor) + ", " + objectCoords.get(cursor + 1));
                    return false;
                }

                //Pathing block 2
                if (pathTo(objectCoords.get(cursor + 2), objectCoords.get(cursor + 3))) {
                    if (debug > 1)
                        System.out.println("✔ Pathing " + objectCoords.get(cursor + 2) + ", " + objectCoords.get(cursor + 3));
                } else {
                    if (debug > 1)
                        System.out.println("✗ Pathing " + objectCoords.get(cursor + 2) + ", " + objectCoords.get(cursor + 3));
                    return false;
                }
                cursor += 4;
            }
        }
        //pathTo(room, coords.get(cursor), coords.get(cursor + 1), coords);
        //System.out.println(pathTo(room, coords.get(3), coords.get(4), coords));
        return true;
    }

    public Boolean pathTo(int x, int y) throws IOException{
        //0 = left, 1 = right, 2 = down, 3 = up (yes, it's switched so it could go opposite from door)
        int count = 0;
        int direction = objectCoords.get(0);
        int doorX = objectCoords.get(1);
        int doorY = objectCoords.get(2);
        int pathX = 0;
        int pathY = 0;
        if (direction == 0) { //Go Left (Door at Right)
            pathX = doorX - 1;
            pathY = doorY;
        }
        if (direction == 1) { //Go Right (Door at Left)
            pathX = doorX + 1;
            pathY = doorY;
        }
        if (direction == 2) { //Go Down (Door Up)
            pathX = doorX;
            pathY = doorY + 1;
        }
        if (direction == 3) { //Go Up (Door Down)
            pathX = doorX;
            pathY = doorY - 1;
        }

        while (count < 1000) {
            //Go in the set direction until hit something
            if (debug > 2) {
                roomGrid[pathY][pathX] = "O";
                printRoom();
                roomGrid[pathY][pathX] = "-";
                System.out.println();
            }

            if (direction == 0) pathX--; //Left

            if (direction == 1) pathX++; //Right

            if (direction == 2) pathY++; //Down

            if (direction == 3) pathY--; //Up

            //Check if hit the target
            if (pathX == x && pathY == y) {
                objectDirections.add((x*10) + y);
                objectDirections.add(direction);
                return true;
            }

            //Back up if hit something
            else if (roomGrid[pathY][pathX] != "-") {
                if (direction == 0) pathX++;
                if (direction == 1) pathX--;
                if (direction == 2) pathY--;
                if (direction == 3) pathY++;
            }

            //Pick new direction
            direction = (int) (Math.random() * 4);
            count++;
        }
//        if (debug > 1) System.out.println("Can't reach " + x + ", " + y);

        return false;
    }
}
