import java.time.LocalDate;
import java.util.*;

public class BookingManager implements BookingManagerInterface
{

    private final Map<Integer, Room> rooms = new HashMap();

    @Override
    public synchronized boolean isRoomAvailable(final Integer room, final LocalDate date)
    {
        if(!rooms.containsKey(room))
        {
            return false;
        }

        final Room roomToCheck = rooms.get(room);
        final Optional<LocalDate> listOfDates = roomToCheck.getDatesBooked().stream().filter(bookingDate -> bookingDate.isEqual(date)).findFirst();

        if(!listOfDates.isEmpty())
        {
            return false;
        }

        return true;
    }

    @Override
    public synchronized void addBooking(final String guest, final Integer room, final LocalDate date)
    {
        if(!isRoomAvailable(room, date))
        {
            throw new BookingException("Room " + room + " is already booked");
        }

        rooms.get(room).addBookingDate(date);
    }

    @Override
    public synchronized Iterable<Integer> getAvailableRooms(final LocalDate date)
    {
        final List<Integer> availableRooms = new ArrayList<>();

        for(Room room : rooms.values())
        {
            if(!room.getDatesBooked().contains(date))
            {
                availableRooms.add(room.getRoomId());
            }
        }

        final Iterable<Integer> iterableRooms = availableRooms;

        return iterableRooms;
    }

    public String addRoom(final int roomNumber)
    {
        if(rooms.containsKey(roomNumber))
        {
            return "Invalid - Room already exists";
        }

        rooms.put(roomNumber, new Room(roomNumber));

        return "SUCCESS";
    }

    public Room getRoom(final int roomNumber)
    {
        return rooms.get(roomNumber);
    }

}
