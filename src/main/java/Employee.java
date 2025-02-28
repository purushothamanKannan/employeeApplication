
public class Employee {

    private String employeeName, employeeDateOfJoin, employeeSalary, employeeQualifications, imageURL;
    private int  employeeAge, employeeExperieance;

    public Employee(String employeeName, int employeeAge, int employeeExperieance,
                    String employeeDateOfJoin, String employeeSalary, String employeeQualifications, String imageURL) {
        super();
        this.employeeName = employeeName;
        this.employeeAge = employeeAge;
        this.employeeExperieance = employeeExperieance;
        this.employeeDateOfJoin = employeeDateOfJoin;
        this.employeeSalary = employeeSalary;
        this.employeeQualifications = employeeQualifications;
        this.imageURL = imageURL;

    }
    public int getEmployeeAge() {
        return employeeAge;
    }
    public void setEmployeeAge(int employeeAge) {
        this.employeeAge = employeeAge;
    }
    public int getEmployeeExperieance() {
        return employeeExperieance;
    }
    public void setEmployeeExperieance(int employeeExperieance) {
        this.employeeExperieance = employeeExperieance;
    }
    public String getEmployeeDateOfJoin() {
        return employeeDateOfJoin;
    }
    public void setEmployeeDateOfJoin(String employeeDateOfJoin) {
        this.employeeDateOfJoin = employeeDateOfJoin;
    }
    public String getEmployeeSalary() {
        return employeeSalary;
    }
    public void setEmployeeSalary(String employeeSalary) {
        this.employeeSalary = employeeSalary;
    }
    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public String getEmployeeQualifications(){
        return employeeQualifications;
    }
    public void setEmployeeQualifications(String employeeQualifications){
        this.employeeQualifications = employeeQualifications;
    }

    public String getImageURL(){
        return imageURL;
    }
    public void setImageURL(String imageURL){
        this.imageURL=imageURL;
    }

}
