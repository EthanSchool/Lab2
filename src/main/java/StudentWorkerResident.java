public class StudentWorkerResident extends Resident {

	public float HourlyPayRate;
	public float MonthlyHoursWorked;

	@Override
	public float GetRent() {
		return (float) (1_245 - 0.5 * HourlyPayRate * MonthlyHoursWorked);
	}
}