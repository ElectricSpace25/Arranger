import java.io.IOException;
import java.util.ArrayList;

public class Room {
    int width;
    int height;
    int doorX = 0;
    int doorY = 0;
    int doorSide = 0;
    String[][] roomGrid;
    int debug = 0;
    //0 = none | 1 = room validity & print | 2 = Object checks | 3 = Pathing visualization
    ArrayList<Integer> objectDirections = new ArrayList<>();
    ArrayList<obj> objects = new ArrayList<>();


    public Room(int width, int height, String type) throws IOException {
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
                System.out.print(roomGrid[i][j]);
                for (int k = 0; k < 7-roomGrid[i][j].length(); k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public String[][] bathroom() throws IOException {
        int tries = 0;
        do {

            if (debug > 0) System.out.println();

            objects = new ArrayList<>();

            //Create empty room
            roomGrid = createRoom(roomGrid[0].length, roomGrid.length);

            //Door
            placeDoor();

            //Bathtub
            obj bath = new obj();
            placeDoubleObj("bath", bath);

            //Toilet
            obj toilet = new obj();
            ;
            placeObj("toilet", toilet);

            //Sink
            obj sink = new obj();
            ;
            placeObj("sink", sink);

            if (debug > 0) printRoom();

            tries++;

        } while (!checkRoom());

        if (debug > 0) System.out.println("Room valid!\nIt took " + tries + " tries/try.\n");
        return roomGrid;

    }

    public void placeObj(String name, obj oName) throws IOException {
        int x = 0;
        int y = 0;
        while (!roomGrid[y][x].equals("-")) {
            x = (int) (Math.random() * (roomGrid[0].length - 2)) + 1;
            y = (int) (Math.random() * (roomGrid.length - 2)) + 1;
        }
        oName.singleObj(name, 1, x, y);
        objects.add(oName);
//        objectCoords.add(x);
//        objectCoords.add(y);
//        objectSizes.add(1);
        roomGrid[y][x] = name;
    }

    public void placeDoubleObj(String name, obj oName) throws IOException {

        //First block
        int x = 0;
        int y = 0;
        while (!roomGrid[y][x].equals("-")) {
            x = (int) (Math.random() * (roomGrid[0].length - 2)) + 1;
            y = (int) (Math.random() * (roomGrid.length - 2)) + 1;
        }
        roomGrid[y][x] = name;

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
        oName.doubleObj(name, 2, x, y, x2, y2);
        objects.add(oName);
        roomGrid[y2][x2] = name;
    }

    public Boolean checkArea(obj object) {
        return (roomGrid[object.y + 1][object.x].equals("-") || roomGrid[object.y][object.x + 1].equals("-") || roomGrid[object.y - 1][object.x].equals("-") || roomGrid[object.y][object.x - 1].equals("-"))
                && !(roomGrid[object.y + 1][object.x].equals("=") || roomGrid[object.y][object.x + 1].equals("=") || roomGrid[object.y - 1][object.x].equals("=") || roomGrid[object.y][object.x - 1].equals("="));
    }

    public Boolean checkDoubleArea(obj object) {
        ArrayList freeSpace = new ArrayList();

        //Block 1
        if (roomGrid[object.y][object.x + 1].equals("-")) {
            freeSpace.add(0);
        }
        if (roomGrid[object.y][object.x - 1].equals("-")) {
            freeSpace.add(1);
        }
        if (roomGrid[object.y - 1][object.x].equals("-")) {
            freeSpace.add(2);
        }
        if (roomGrid[object.y + 1][object.x].equals("-")) {
            freeSpace.add(3);
        }
        if (roomGrid[object.y + 1][object.x].equals("=") || roomGrid[object.y][object.x + 1].equals("=") || roomGrid[object.y - 1][object.x].equals("=") || roomGrid[object.y][object.x - 1].equals("="))
            return false;

        //Block 2
        if (roomGrid[object.y2][object.x2 + 1].equals("-")) {
            freeSpace.add(0);
        }
        if (roomGrid[object.y2][object.x2 - 1].equals("-")) {
            freeSpace.add(1);
        }
        if (roomGrid[object.y2 - 1][object.x2].equals("-")) {
            freeSpace.add(2);
        }
        if (roomGrid[object.y2 + 1][object.x2].equals("-")) {
            freeSpace.add(3);
        }
        if (roomGrid[object.y2 + 1][object.x2].equals("=") || roomGrid[object.y2][object.x2 + 1].equals("=") || roomGrid[object.y2 - 1][object.x2].equals("=") || roomGrid[object.y2][object.x2 - 1].equals("="))
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
        doorSide = side;

        int rand;

        if (side == 0 || side == 1) {
            rand = (int) (Math.random() * (roomGrid.length - 2)) + 1;
            if (side == 0) {
                roomGrid[rand][roomGrid[0].length - 1] = "=";
                doorX = roomGrid[0].length - 1;
                doorY = rand;
            } else {
                roomGrid[rand][0] = "=";
                doorX = 0;
                doorY = rand;
            }
        } else {
            rand = (int) (Math.random() * (roomGrid[0].length - 2)) + 1;
            if (side == 2) {
                roomGrid[0][rand] = "=";
                doorX = rand;
                doorY = 0;
            } else {
                roomGrid[roomGrid[0].length - 1][rand] = "=";
                doorX = rand;
                doorY = roomGrid[0].length - 1;
            }
        }

        return side;
    }

    public Boolean checkRoom() throws IOException {
        //TODO: Change fori to foreach
        if (debug > 0) System.out.println("Checking room...");
        int cursor = 3; // 0 = door side, 1 = door x, 2 = door y
        for (int i = 0; i < objects.size(); i++) {
            //Single object
            if (objects.get(i).size == 1) {
                if (debug > 1) System.out.println("Checking " + objects.get(i).x + ", " + objects.get(i).y);

                //Area
                if (checkArea(objects.get(i))) {
                    if (debug > 1) System.out.println("✔ Area");
                } else {
                    if (debug > 1) System.out.println("✗ Area");
                    if (debug > 0) System.out.println("Room invalid");
                    return false;
                }

                //Pathing
                if (pathTo(objects.get(i), objects.get(i).x, objects.get(i).y)) {
                    if (debug > 1) System.out.println("✔ Pathing");
                } else {
                    if (debug > 1) System.out.println("✗ Pathing");
                    return false;
                }
            }
            //Double object
            if (objects.get(i).size == 2) {
                if (debug > 1)
                    System.out.println("Checking " + objects.get(i).x + ", " + objects.get(i).x + " and " + objects.get(i).x2 + ", " + objects.get(i).y2);

                //Area
                if (checkDoubleArea(objects.get(i))) {
                    if (debug > 1) System.out.println("✔ Area");
                } else {
                    if (debug > 1) System.out.println("✗ Area");
                    if (debug > 0) System.out.println("Room invalid");
                    return false;
                }

                //Pathing block 1
                if (pathTo(objects.get(i), objects.get(i).x, objects.get(i).y)) {
                    if (debug > 1)
                        System.out.println("✔ Pathing " + objects.get(i).x + ", " + objects.get(i).y);
                } else {
                    if (debug > 1)
                        System.out.println("✗ Pathing " + objects.get(i).x + ", " + objects.get(i).y);
                    return false;
                }

                //Pathing block 2
                if (pathTo(objects.get(i), objects.get(i).x2, objects.get(i).y2)) {
                    if (debug > 1)
                        System.out.println("✔ Pathing " + objects.get(i).x2 + ", " + objects.get(i).y2);
                } else {
                    if (debug > 1)
                        System.out.println("✗ Pathing " + objects.get(i).x2 + ", " + objects.get(i).y2);
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean pathTo(obj object, int x, int y) throws IOException {
        //0 = left, 1 = right, 2 = down, 3 = up (yes, it's switched so it could go opposite from door)
        int direction = doorSide;
        int count = 0;
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
                object.direction = direction;
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
