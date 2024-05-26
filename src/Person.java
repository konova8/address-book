public class Person {
    protected String name;
    protected String surname;
    protected String address;
    protected String phoneNumber;
    protected int age;

    Person(String name, String surname, String address, String phoneNumber, int age) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
    }

    public String toString() {
        return String.format("Person(%s, %s, %s, %s, %d)", name, surname, address, phoneNumber, age);
    }

    public String toCSVString() {
        return String.format("%s;%s;%s;%s;%d", name, surname, address, phoneNumber, age);
    }
}