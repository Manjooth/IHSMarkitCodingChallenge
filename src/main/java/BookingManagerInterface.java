import java.time.LocalDate;

public interface BookingManagerInterface
{

    public boolean isRoomAvailable(Integer room, LocalDate date);
    public void addBooking(String guest, Integer room, LocalDate date);
    public Iterable<Integer> getAvailableRooms(LocalDate date);
}
