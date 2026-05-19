package officemanagementsystem.entity;

public class Employee {

    private String id;
    private String name;
    private String age;
    private String rank;

    public Employee(String id, String name, String age, String rank) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.rank = rank;
    }
   
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }


    public String getRank() {
        return rank;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name; 
    }

    public void setAge(String age) {
        this.age = age; 
    }

    public void setRank(String rank) {
        this.rank = rank; 
    }

    public String toLine() {
    
        return id + "," + name + "," + age + "," + rank;
    }

    public static Employee fromLine(String line) {

        if (line == null)
            return null;

        String[] data = line.split(",", -1);

        if (data.length != 4)
            return null; 

        return new Employee(data[0], data[1], data[2], data[3]);
    }

    public Object[] toRow() {
        
        return new Object[] { id, name, age, rank };
    }
}
