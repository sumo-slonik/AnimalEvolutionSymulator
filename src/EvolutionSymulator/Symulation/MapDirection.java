package Symulation;

public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NorthEast,
    NorthWest,
    SouthEast,
    SouthWest;

    //convert vector to string
    public String toString(){
        return switch (this) {
            case EAST -> "E";
            case WEST -> "W";
            case NORTH -> "N";
            case SOUTH -> "S";
            case NorthEast -> "NE";
            case NorthWest -> "NW";
            case SouthEast -> "SE";
            case SouthWest -> "SW";
        };
    }
    public MovieDirection toMovieDirection()
    {
        return switch(this) {
            case EAST -> MovieDirection.RIGHT;
            case WEST -> MovieDirection.LEFT;
            case NORTH -> MovieDirection.FORWARD;
            case SOUTH -> MovieDirection.BACKWARD;
            case SouthWest -> MovieDirection.BottomLeftBevel;
            case SouthEast -> MovieDirection.BottomRightBevel;
            case NorthWest -> MovieDirection.TopLeftBevel;
            case NorthEast -> MovieDirection.TopRightBevel;
        };
    }
    //returns the next direction clockwise
    @SuppressWarnings("DuplicatedCode")
    public MapDirection next(){
        return switch(this){
            case NORTH -> NorthEast;
            case NorthEast -> EAST;
            case EAST -> SouthEast;
            case SouthEast -> SOUTH;
            case SOUTH -> SouthWest;
            case SouthWest -> WEST;
            case WEST -> NorthWest;
            case NorthWest -> NORTH;
        };
    }
    //returns the next direction counterclockwise
    @SuppressWarnings("DuplicatedCode")
    public MapDirection previous(){
        return switch(this){
            case NORTH -> NorthWest;
            case NorthWest -> WEST;
            case WEST -> SouthWest;
            case SouthWest -> SOUTH;
            case SOUTH -> SouthEast;
            case SouthEast -> EAST;
            case EAST -> NorthEast;
            case NorthEast -> NORTH;
        };
    }
    //returns a unit vector in the specified direction
    public Vector2d toOneStepVector(){
        return switch(this){
            case EAST -> new Vector2d(1,0);
            case SOUTH -> new Vector2d(0,-1);
            case WEST -> new Vector2d(-1,0);
            case NORTH -> new Vector2d(0,1);
            case NorthEast -> new Vector2d(1,1);
            case NorthWest -> new Vector2d(-1,1);
            case SouthEast -> new Vector2d(1,-1);
            case SouthWest -> new Vector2d(-1,-1);
        };
    }
}
