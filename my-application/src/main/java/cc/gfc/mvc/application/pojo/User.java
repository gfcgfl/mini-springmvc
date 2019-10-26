package cc.gfc.mvc.application.pojo;

/**
 * @ClassName: user
 * @Description:
 * @CreatedBy: fcguo
 * @CreatedAt: 6/20/19 3:31 PM
 **/
public class User {
    private String name;
    private String salary;

    public User(String name, String salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }
}
