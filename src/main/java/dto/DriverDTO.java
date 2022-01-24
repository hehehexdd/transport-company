package dto;

import java.math.BigDecimal;

public class DriverDTO {

    private long id;

    private String name;

    private BigDecimal salary;

    public DriverDTO() {
    }

    public DriverDTO(long id, String name, BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "DriverDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
