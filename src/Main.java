//TODO: Don't allow area in front of door to be used as freespace
//TODO: Turn room into a class to have multiple rooms and have 'global variables'
//TODO: Go graphics!
//TODO: Consider switching to GitHub (turn TODOs to Issues, keep track of changes, good practice, good for portfolios :P)
//TODO: Add an object param for trying to generate against a wall
//TODO: Add wall objects, but switch to graphics first cuz otherwise it'd be hard to tell what side of the wall the mirror is on
//TODO: Timeout for generating room
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {

        int width = 3+2;
        int height = 3+2;

        Room bathroom = new Room(width, height,"bathroom");
        RoomDrawer.draw(bathroom.roomGrid, bathroom.width, bathroom.height, bathroom.objectDirections, bathroom.objectCoords);

    }

}

/*

 */