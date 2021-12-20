import java.time.LocalDate;
import java.util.ArrayList;

public class Room
{

    private final int roomId;
    private final ArrayList<LocalDate> datesBooked = new ArrayList<>();

    public Room(final int roomId)
    {
        this.roomId = roomId;
    }

    public int getRoomId()
    {
        return roomId;
    }

    public ArrayList<LocalDate> getDatesBooked()
    {
        return datesBooked;
    }

    public void addBookingDate(final LocalDate datesBooked)
    {
        this.datesBooked.add(datesBooked);
    }

}
