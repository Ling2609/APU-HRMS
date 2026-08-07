package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "salarylog")
public class SalaryLog implements Serializable  { 
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salarylog_seq")
    @SequenceGenerator(name = "salarylog_seq", sequenceName = "salarylog_seq", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;
    
    @Column(nullable = false)
    private double salary;

    public SalaryLog() {}

    public SalaryLog(User user, Report report) {
        this.user = user;
        this.salary = user.getSalary();
        this.report = report;
    }
    
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return this.user; }
    public void setUser(User user) { this.user = user; }
    
    public double getUserSalary() { return this.salary; }
    public void setUserSalary(double salary) { this.salary = salary; }
    
    public Long getReportID() { return this.report.getId(); }
    public void setReportID(Long input) { this.report.setId(input); }
    
}