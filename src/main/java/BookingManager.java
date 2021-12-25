import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BookingManager implements BookingManagerInterface
{
    /**
     ConcurrentHashMap class is thread-safe --> multiple threads
     can operate on a single object without any complications
     **/
    private final Map<Integer, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public boolean isRoomAvailable(final Integer room, final LocalDate date)
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
    public void addBooking(final String guest, final Integer room, final LocalDate date)
    {
        if(!isRoomAvailable(room, date))
        {
            throw new BookingException("Room " + room + " is already booked");
        }

        rooms.get(room).addBookingDate(date);
    }

    @Override
    public Iterable<Integer> getAvailableRooms(final LocalDate date)
    {
        final List<Integer> availableRooms = rooms.values().stream()
                .filter(room -> !room.getDatesBooked().contains(date))
                .map(Room::getRoomId)
                .collect(Collectors.toList());

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

    public int getSize()
    { // method to help testing
        return rooms.size();
    }

}
