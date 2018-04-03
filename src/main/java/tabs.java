import org.mongodb.morphia.query.Query;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class tabs {
	public JPanel tabbedPanel;
	private JTabbedPane tabbedPane1;
	private JButton searchButton;
	private JTable table1;
	private JFormattedTextField firstNameField;
	private JButton addResidentButton;
	private JComboBox residentTypeBox;
	private JFormattedTextField lastNameField;
	private JComboBox dormFloorBox;
	private JFormattedTextField dormNumberField;
	private JTextField residentId;
	private JTextField hourlyPayField;
	private JTextField monthlyHoursWorkField;
	private JButton refreshButton;
	private JLabel totalResidentsLabel;
	private JLabel totalRevenueLabel;
//	private JComboBox searchComboBox;

	public ResidentType SelectedResidentType;

	public tabs() {
		residentTypeBox.addItem("");
		residentTypeBox.addItem("Student Worker");
		residentTypeBox.addItem("Scholarship Recipient");
		residentTypeBox.addItem("Athlete");

		residentTypeBox.addItemListener(e -> {
			dormFloorBox.removeAllItems();

			hourlyPayField.setText("");
			hourlyPayField.setEnabled(false);
			monthlyHoursWorkField.setText("");
			monthlyHoursWorkField.setEnabled(false);

			switch((String) e.getItem()) {
				case "Student Worker":
					hourlyPayField.setEnabled(true);
					monthlyHoursWorkField.setEnabled(true);

					dormFloorBox.setModel(new DefaultComboBoxModel(new Integer[]{1, 2, 3}));
					SelectedResidentType = ResidentType.StudentWorker;
					break;
				case "Scholarship Recipient":
					dormFloorBox.setModel(new DefaultComboBoxModel(new Integer[]{7, 8}));
					SelectedResidentType = ResidentType.Scholarship;
					break;
				case "Athlete":
					dormFloorBox.setModel(new DefaultComboBoxModel(new Integer[]{4, 5, 6}));
					SelectedResidentType = ResidentType.Athlete;
					break;
				default:
					dormFloorBox.setModel(new DefaultComboBoxModel(new Integer[]{10}));
					SelectedResidentType = ResidentType.Invalid;
					break;
			}
		});

		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);

		formatter.setCommitsOnValidEdit(true);
		dormNumberField.setFormatterFactory(new DefaultFormatterFactory(formatter));

		searchButton.addActionListener(e -> {
			Query<Resident> query = Mongo.datastore.createQuery(Resident.class);

			List<Object[]> objects = new ArrayList<>();
			query.forEach(resident -> objects.add(resident.ToObjectArray()));
			table1.setModel(new DefaultTableModel(objects.toArray(new Object[0][]), Resident.Labels()));
		});


		addResidentButton.addActionListener(e -> {
			String error = "";
			short id = 0;
			try {
				id = Short.parseShort(residentId.getText());
				if(id < 0)
					error += "Id out of rage must be between 0 and 999,999\n";
			} catch(NumberFormatException ex) {
				error += "Invalid Id must be between 0 and 999,999\n";
			}

			String firstName = firstNameField.getText();
			if(firstName.length() == 0)
				error += "Please specify a first name\n";
			String lastName = lastNameField.getText();
			if(lastName.length() == 0)
				error += "Please specify a last name\n";
			int dormNumber = 0;
			try {
				dormNumber = Integer.parseInt(dormNumberField.getText());
				if(dormNumber < 0 || dormNumber > 999_999)
					error += "Dorm Number out of rage must be between 0 and 999,999\n";
			} catch(NumberFormatException ex) {
				error += "Invalid Dorm Number must be between 0 and 999,999\n";
			}

			Resident resident = null;

			switch(SelectedResidentType) {
				case Scholarship:
					resident = new ScholarshipResident();
					break;
				case Athlete:
					resident = new AthleteResident();
					break;
				case StudentWorker:
					resident = new StudentWorkerResident();

					float hourlyPay = 0;
					try {
						hourlyPay = Float.parseFloat(hourlyPayField.getText());
						if(hourlyPay < 0)
							error += "Hourly Pay out of range it must be positive\n";
					} catch(NumberFormatException ex) {
						error += "Invalid Hourly Pay it must be a positive number\n";
					}

					((StudentWorkerResident) resident).HourlyPayRate = hourlyPay;

					float hoursWorked = 0;
					try {
						hoursWorked = Float.parseFloat(monthlyHoursWorkField.getText());
						if(hoursWorked < 0)
							error += "Hours worked out of range it must be positive\n";
					} catch(NumberFormatException ex) {
						error += "Invalid Hours worked it must be a positive number\n";
					}
					((StudentWorkerResident) resident).MonthlyHoursWorked = hoursWorked;
					break;
				default:
					error += "Invalid Resident Type\n";
					break;
			}

			if(Mongo.datastore.createQuery(Resident.class).field("_id").equal(id).count() != 0) {
				error += "Student already exits with id\n";
			}

			if(!error.equals("")) {
				JOptionPane.showMessageDialog(null, error);
				return;
			}

			resident.Id = id;
			resident.FirstName = firstName;
			resident.LastName = lastName;
			resident.DormFloor = (Integer) dormFloorBox.getSelectedItem();
			resident.DormRoom = dormNumber;

			Mongo.datastore.save(resident);

			JOptionPane.showMessageDialog(null, "Student successfully added!");
		});

		refreshButton.addActionListener(this::RefreshStats);
		RefreshStats();
	}

	private void RefreshStats(ActionEvent e) {
		RefreshStats();
	}

	private void RefreshStats() {
		Query<Resident> query = Mongo.datastore.createQuery(Resident.class);
		totalResidentsLabel.setText(Long.toString(query.count()));
		AtomicReference<Float> totalRevenue = new AtomicReference<>((float) 0);
		query.forEach(resident -> totalRevenue.updateAndGet(v -> (float) (v + resident.GetRent())));
		totalRevenueLabel.setText("$" + totalRevenue);
	}
}