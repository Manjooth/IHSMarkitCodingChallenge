### Implementation Decisions
- Using `Map<Integer, Room>` in order to store `<Room Number, Room Object>` for easy access
  and quick retrieval of objects from the `Map`.
- Creating `Room` object to store room number and a `List` of the dates the room has been booked.
- Do nothing with guest surname as we are currently not using this anywhere.
- Creating `BookingException` to give a more meaningful error message.

### Requirements:

- Requirement 1: implement `isRoomAvailable()`

In order to get this to work and test this, `addRoom()` method has been implemnted in order to add rooms
to the booking system, this method also verifies is the room number is unique. Fetch item, if
it exists, from the `Map` and check booked dates.

- Requirement 2: implement `addBooking()`

If `isRoomAvailable()` returns true, add booking date into `Room` objects `List`, otherwise 
throw new `BookingException`.

- Requirement 3: implement `getAvailableRooms()`

Loop through rooms in `Map`. If the Room objects `List` of booked dates does not contain the date, add to `List`
of available rooms. Convert this into an `Iterable List` and return the value.

- Requirement 4: Make thread safe

Added `synchronised` keyword to method signatures to make methods thread safe.