package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class AttendanceSheetTest {

    private AttendanceSheet attendanceSheetEmpty;
    private AttendanceSheet attendanceSheet;

    private LinkedList emptyLinkedList;

    private Registry registry;

    private Caregiver cg1;
    private Caregiver cg2;
    private Caregiver cg3;
    private Caregiver cg4;
    private Caregiver cg5;

    private Child c1;
    private Child c2;
    private Child c3;
    private Child c4;
    private Child c5;

    @BeforeEach
    public void setup() {
        attendanceSheetEmpty = new AttendanceSheet("Field Trip");
        attendanceSheet = new AttendanceSheet("Class Attendance");

        emptyLinkedList = new LinkedList();

        registry = new Registry("CPSC 210");

        cg1 = new Caregiver("Ada Lovelace", 7788221234L, "alovelace@gmail.com");
        cg2 = new Caregiver("John Smith", 6043456789L, "jsmith@yahoo.ca");
        cg3 = new Caregiver("Adam Lovelace", 7788225678L, "adaml@gmail.com");
        cg4 = new Caregiver("Charlie Santos", 6041234567L, "charlesss@yahoo.ca");
        cg5 = new Caregiver("Janet Smith", 7785658897L, "jansmith@yahoo.com");

        c1 = new Child("Jane Lovelace", cg1);
        c2 = new Child("David Lovelace", cg1);
        c3 = new Child("Emily Smith", cg2);
        c4 = new Child("Lily Santos", cg4);
        c5 = new Child("Elisa Smith", cg5);

        registry.getChildRegistry().add(c1);
        registry.getChildRegistry().add(c2);
        registry.getChildRegistry().add(c3);
        registry.getChildRegistry().add(c4);

        attendanceSheet.notCheckedIn(c1);
        attendanceSheet.notCheckedIn(c2);
        attendanceSheet.notCheckedIn(c3);

        c1.addAuthorizedToPickUp(cg3);
        c2.addAuthorizedToPickUp(cg3);
        c3.addAuthorizedToPickUp(cg5);
    }

    @Test
    public void testConstructor() {
        assertEquals("Field Trip", attendanceSheetEmpty.getName());
        assertEquals(emptyLinkedList, attendanceSheetEmpty.getNotCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheetEmpty.getCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheetEmpty.getCheckedOut());
    }

    @Test
    public void testNotCheckedIn() {
        assertEquals(c1, attendanceSheet.getNotCheckedIn().get(0));
        assertEquals(c2, attendanceSheet.getNotCheckedIn().get(1));
        assertEquals(c3, attendanceSheet.getNotCheckedIn().get(2));

        assertEquals(emptyLinkedList, attendanceSheet.getCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheet.getCheckedOut());
    }

    @Test
    public void testCheckInTrue() {
        assertEquals(emptyLinkedList, attendanceSheet.getCheckedIn());
        assertEquals(3, attendanceSheet.getNotCheckedIn().size());
        assertEquals(c1, attendanceSheet.getNotCheckedIn().get(0));
        assertEquals(c2, attendanceSheet.getNotCheckedIn().get(1));
        assertEquals(c3, attendanceSheet.getNotCheckedIn().get(2));

        assertTrue(attendanceSheet.checkIn(c1));
        assertEquals(c1, attendanceSheet.getCheckedIn().get(0));
        assertEquals(c2, attendanceSheet.getNotCheckedIn().get(0));
        assertEquals(c3, attendanceSheet.getNotCheckedIn().get(1));
        assertEquals(2, attendanceSheet.getNotCheckedIn().size());

        assertTrue(attendanceSheet.checkIn(c2));
        assertEquals(c1, attendanceSheet.getCheckedIn().get(0));
        assertEquals(c2, attendanceSheet.getCheckedIn().get(1));
        assertEquals(c3, attendanceSheet.getNotCheckedIn().get(0));
        assertEquals(1, attendanceSheet.getNotCheckedIn().size());

        assertTrue(attendanceSheet.checkIn(c3));
        assertEquals(c1, attendanceSheet.getCheckedIn().get(0));
        assertEquals(c2, attendanceSheet.getCheckedIn().get(1));
        assertEquals(c3, attendanceSheet.getCheckedIn().get(2));
        assertEquals(emptyLinkedList, attendanceSheet.getNotCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheet.getCheckedOut());
    }

    @Test
    public void testCheckInChildFalse() {
        assertFalse(attendanceSheetEmpty.checkIn(c1));
        assertFalse(attendanceSheetEmpty.checkIn(c2));
        assertFalse(attendanceSheetEmpty.checkIn(c3));
        assertEquals(emptyLinkedList, attendanceSheetEmpty.getNotCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheetEmpty.getCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheetEmpty.getCheckedOut());

        assertFalse(attendanceSheet.checkIn(c4));
        assertFalse(attendanceSheet.checkIn(c5));
        assertEquals(c1, attendanceSheet.getNotCheckedIn().get(0));
        assertEquals(c2, attendanceSheet.getNotCheckedIn().get(1));
        assertEquals(c3, attendanceSheet.getNotCheckedIn().get(2));
        assertEquals(emptyLinkedList, attendanceSheet.getCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheet.getCheckedOut());
    }

    @Test
    public void testIsAuthorizedToPickUpTrue() {
        assertTrue(attendanceSheet.isAuthorizedToPickUp(cg1, c1));
        assertTrue(attendanceSheet.isAuthorizedToPickUp(cg3, c1));
        assertTrue(attendanceSheet.isAuthorizedToPickUp(cg1, c2));
        assertTrue(attendanceSheet.isAuthorizedToPickUp(cg3, c2));
        assertTrue(attendanceSheet.isAuthorizedToPickUp(cg2, c3));
        assertTrue(attendanceSheet.isAuthorizedToPickUp(cg5, c3));
    }

    @Test
    public void testIsAuthorizedToPickUpFalse() {
        assertFalse(attendanceSheet.isAuthorizedToPickUp(cg2, c1));
        assertFalse(attendanceSheet.isAuthorizedToPickUp(cg4, c1));
        assertFalse(attendanceSheet.isAuthorizedToPickUp(cg2, c2));
        assertFalse(attendanceSheet.isAuthorizedToPickUp(cg5, c2));
        assertFalse(attendanceSheet.isAuthorizedToPickUp(cg1, c3));
        assertFalse(attendanceSheet.isAuthorizedToPickUp(cg4, c3));
    }

    @Test
    public void testChildIsCheckedInTrue() {
        assertTrue(attendanceSheet.checkIn(c1));
        assertTrue(attendanceSheet.checkIn(c2));
        assertTrue(attendanceSheet.checkIn(c3));

        assertTrue(attendanceSheet.childIsCheckedIn(c1));
        assertTrue(attendanceSheet.childIsCheckedIn(c2));
        assertTrue(attendanceSheet.childIsCheckedIn(c3));
    }

    @Test
    public void testChildIsCheckedInFalse() {
        assertFalse(attendanceSheet.childIsCheckedIn(c1));
        assertFalse(attendanceSheet.childIsCheckedIn(c2));
        assertFalse(attendanceSheet.childIsCheckedIn(c3));
        assertFalse(attendanceSheet.childIsCheckedIn(c4));
    }

    @Test
    public void testChildIsNotCheckedInTrue() {
        assertTrue(attendanceSheet.childIsNotCheckedIn(c1));
        assertTrue(attendanceSheet.childIsNotCheckedIn(c2));
        assertTrue(attendanceSheet.childIsNotCheckedIn(c3));
    }

    @Test
    public void testChildIsNotCheckedInFalse() {
        assertFalse(attendanceSheet.childIsNotCheckedIn(c4));
        assertFalse(attendanceSheet.childIsNotCheckedIn(c5));
    }

    @Test
    public void testCheckOutCaregiverNull() {
        assertTrue(attendanceSheet.checkIn(c1));
        assertTrue(attendanceSheet.checkIn(c2));
        assertTrue(attendanceSheet.checkIn(c3));

        assertEquals("caregiver null", attendanceSheet.checkOut(c1, null));
        assertEquals("caregiver null", attendanceSheet.checkOut(c2, null));
        assertEquals("caregiver null", attendanceSheet.checkOut(c3, null));
    }

    @Test
    public void testCheckOutSuccess() {
        assertTrue(attendanceSheet.checkIn(c1));
        assertTrue(attendanceSheet.checkIn(c2));
        assertTrue(attendanceSheet.checkIn(c3));

        assertEquals("child checked out", attendanceSheet.checkOut(c1, cg1));
        assertEquals("child checked out", attendanceSheet.checkOut(c2, cg3));
        assertEquals("child checked out", attendanceSheet.checkOut(c3, cg2));
    }

    @Test
    public void testCheckOutChildNotCheckedIn() {
        assertEquals("child not checked in", attendanceSheet.checkOut(c1, cg3));
        assertEquals("child not checked in", attendanceSheet.checkOut(c2, cg1));
        assertEquals("child not checked in", attendanceSheet.checkOut(c3, cg5));
        assertEquals("child not checked in", attendanceSheet.checkOut(c4, cg4));
    }

    @Test
    public void testCheckOutCaregiverNotAuthorized() {
        assertTrue(attendanceSheet.checkIn(c1));
        assertTrue(attendanceSheet.checkIn(c2));
        assertTrue(attendanceSheet.checkIn(c3));

        assertEquals("caregiver not authorized", attendanceSheet.checkOut(c1, cg2));
        assertEquals("caregiver not authorized", attendanceSheet.checkOut(c2, cg5));
        assertEquals("caregiver not authorized", attendanceSheet.checkOut(c3, cg1));
    }

    @Test
    public void testCheckOutCaregiverNotAuthorizedAndNotCheckedIn() {
        assertEquals("child not checked in and caregiver not authorized", attendanceSheet.checkOut(c1, cg2));
        assertEquals("child not checked in and caregiver not authorized", attendanceSheet.checkOut(c2, cg5));
        assertEquals("child not checked in and caregiver not authorized", attendanceSheet.checkOut(c3, cg1));
    }

    @Test
    public void testResetConfirmed() {
        assertTrue(attendanceSheet.checkIn(c1));
        assertTrue(attendanceSheet.checkIn(c2));
        assertTrue(attendanceSheet.checkIn(c3));

        attendanceSheet.reset("yes", registry.getChildRegistry());

        assertEquals(c1, attendanceSheet.getNotCheckedIn().get(0));
        assertEquals(c2, attendanceSheet.getNotCheckedIn().get(1));
        assertEquals(c3, attendanceSheet.getNotCheckedIn().get(2));
        assertEquals(c4, attendanceSheet.getNotCheckedIn().get(3));
        assertEquals(emptyLinkedList, attendanceSheet.getCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheet.getCheckedOut());

        attendanceSheetEmpty.reset("yes", registry.getChildRegistry());

        assertEquals(c1, attendanceSheet.getNotCheckedIn().get(0));
        assertEquals(c2, attendanceSheet.getNotCheckedIn().get(1));
        assertEquals(c3, attendanceSheet.getNotCheckedIn().get(2));
        assertEquals(c4, attendanceSheet.getNotCheckedIn().get(3));
        assertEquals(emptyLinkedList, attendanceSheet.getCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheet.getCheckedOut());
    }

    @Test
    public void testResetRejected() {
        assertTrue(attendanceSheet.checkIn(c1));
        assertTrue(attendanceSheet.checkIn(c2));
        assertTrue(attendanceSheet.checkIn(c3));

        attendanceSheet.reset("no", registry.getChildRegistry());

        assertEquals(c1, attendanceSheet.getCheckedIn().get(0));
        assertEquals(c2, attendanceSheet.getCheckedIn().get(1));
        assertEquals(c3, attendanceSheet.getCheckedIn().get(2));
        assertEquals(emptyLinkedList, attendanceSheet.getNotCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheet.getCheckedOut());

        attendanceSheetEmpty.reset("tee hee!", registry.getChildRegistry());

        assertEquals(emptyLinkedList, attendanceSheetEmpty.getNotCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheetEmpty.getCheckedIn());
        assertEquals(emptyLinkedList, attendanceSheetEmpty.getCheckedOut());
    }

    @Test
    public void testAddNotCheckedIn() {
        attendanceSheetEmpty.addNotCheckedIn(c1);
        attendanceSheetEmpty.addNotCheckedIn(c2);
        attendanceSheetEmpty.addNotCheckedIn(c3);

        assertTrue(attendanceSheetEmpty.childIsNotCheckedIn(c1));
        assertTrue(attendanceSheetEmpty.childIsNotCheckedIn(c2));
        assertTrue(attendanceSheetEmpty.childIsNotCheckedIn(c3));
    }

    @Test
    public void testAddCheckedIn() {
        attendanceSheetEmpty.addCheckedIn(c1);
        attendanceSheetEmpty.addCheckedIn(c2);
        attendanceSheetEmpty.addCheckedIn(c3);

        assertTrue(attendanceSheetEmpty.childIsCheckedIn(c1));
        assertTrue(attendanceSheetEmpty.childIsCheckedIn(c2));
        assertTrue(attendanceSheetEmpty.childIsCheckedIn(c3));
    }

    @Test
    public void testAddCheckedOut() {
        attendanceSheetEmpty.addCheckedOut(c1);
        attendanceSheetEmpty.addCheckedOut(c2);
        attendanceSheetEmpty.addCheckedOut(c3);

        assertEquals(c1, attendanceSheetEmpty.getCheckedOut().get(0));
        assertEquals(c2, attendanceSheetEmpty.getCheckedOut().get(1));
        assertEquals(c3, attendanceSheetEmpty.getCheckedOut().get(2));
    }
}
