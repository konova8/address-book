public class Contact extends Person {
    protected int id;

    Contact(String name, String surname, String address, String phoneNumber, int age, int id) {
        super(name, surname, address, phoneNumber, age);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.format("Contact(%s, %s, %s, %s, %d, %d)", name, surname, address, phoneNumber, age, id);
    }
}
