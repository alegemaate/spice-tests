import java.util.Scanner;

/*
 * ServiceCommands
 * Validation for Services list
 * Spice Tests
 * 13/10/2018
 */
public class ServiceCommands {

	public static Scanner scan = new Scanner(System.in);
	
	//---------------------------------------------------------------------------------------------
	
	/*
	 * CREATE SERVICE: Prompts user for new service number/name/date
	 *                 Ensures valid service number/name/date
	 *                 Ensures unique service number/name/date
	 *                 Calls Services.add with the unique service number
	 * 
	 * Input: none
	 * Output: int representing return status (0 if successful, -1 otherwise)
	 */
	public static int createService() {
		// Ensure logged in
		if (!CredentialCommands.loggedIn) {
			System.out.println("Error: You are not logged in.");
			return 0;
		} // end if
		
		// Creating a service is only a planner operation
		if (!(CredentialCommands.userType.equals("planner"))) {
			System.out.println("Cannot create a service except during planner session.");
			return -1;
		} // end if
		
		// Prompt user for new service number
		String serviceNumString = ScannerWrapper.getInput("Enter a new service number: ");
		// Ensures valid service number
		int serviceNumber = validateServiceNum(serviceNumString);
		Services.add(serviceNumber);
		
		// Prompt user for new service name
		String serviceName = ScannerWrapper.getInput("Enter a new service name: ");
		serviceName = validateServiceName(serviceName);
		
		// Prompt user for service date
		int year = Integer.parseInt(ScannerWrapper.getInput("Enter year: "));
		int month = Integer.parseInt(ScannerWrapper.getInput("Enter month: "));
		int day = Integer.parseInt(ScannerWrapper.getInput("Enter day: "));
		Date date = new Date(year, month, day);
		
		/*
		 * Add transaction to Log: CCC AAAA MMMM BBBB NNNNNN YYYYMMDD where:
		 * 		a) CCC = "CRE" for create service
		 * 		b) AAAA = serviceNumber
		 * 		c) MMMM = 0 (ticket numbers is unused field)
		 * 		d) BBBB = 00000 (destination service number is unused field)
		 * 		e) NNNNNN = serviceName
		 * 		f) YYYYMMDD = date
		 */
		String logEntry = "CRE" + " " // Create service
						+ Integer.toString(serviceNumber) + " " // Service number
						+ "0" + " " // Number of tickets (unused field)
						+ "00000" + " " // Destination service number (unused field)
						+ serviceName + " "// Service name
						+ date.toString() + " "; // Service date
		Log.addLine(logEntry);
		return 0;
	} // end createService method
	
	//---------------------------------------------------------------------------------------------
	
	/*
	 * DELETESERVICE: Prompts user for existing service number
	 *                Ensures existing service number
     *                Calls Services.remove with the unique service number
     * 
     * Input: none
	 * Output: int representing return status (0 if successful, -1 otherwise)
	 */
	public static int deleteService() {
		// Ensure logged in
		if (!CredentialCommands.loggedIn) {
			System.out.println("Error: You are not logged in.");
			return 0;
		} // end if
		
		// Deleting a service is only a planner operation
		if (!(CredentialCommands.userType.equals("planner"))) {
			System.out.println("Cannot delete a service except during planner session.");
			return -1;
		} // end if
		
		// Prompt user for new service number
		String serviceNumString = ScannerWrapper.getInput("Enter an existing service number: ");
		int serviceNumber = Integer.parseInt(serviceNumString);
		
		// Ensure that service number exists and isn't already deleted
		if (Services.find(serviceNumber) == false) { 
			System.out.println("Service number does not exist; cannot delete.");
			return -1;
		} // end if
		
		// Remove service number from serviceList
		Services.remove(serviceNumber);
		
		// Ask for service name
		String serviceName = ScannerWrapper.getInput("Enter service name: ");
		
		/*
		 * Add transaction to Log: CCC AAAA MMMM BBBB NNNNNN YYYYMMDD where:
		 * 		a) CCC = "DEL" for create service
		 * 		b) AAAA = serviceNumber
		 * 		c) MMMM = 0 (ticket numbers is unused field)
		 * 		d) BBBB = 00000 (destination service number is unused field)
		 * 		e) NNNNNN = serviceName
		 * 		f) YYYYMMDD = 0 (date is unused field)
		 */
		String logEntry = "DEL" + " "
		                + Integer.toString(serviceNumber) + " "
		                + "0" + " "
		                + "00000" + " "
				        + serviceName + " "
				        + "0";
		Log.addLine(logEntry);
		
		// Return successfully
		return 0;
	} // end deleteService method
	
	//---------------------------------------------------------------------------------------------
	
	/*
	 * VALIDATESERVICENUM: Ensures that service number is valid.
	 * CONSTRAINTS:
	 * 		a) Exactly 5 decimal digits
	 * 		b) Must be unique
	 * 		c) Cannot begin with 0
	 * 
	 * Input: none
	 * Output: int representing -1 if service number already exists, service number otherwise
	 */
	private static int validateServiceNum(String serviceNum) {
		// Ensure logged in
		if (!CredentialCommands.loggedIn) {
			System.out.println("Error: You are not logged in.");
			return 0;
		} // end if
		
		
		// Service number must be exactly 5 characters
		while (serviceNum.length() != 5) {
			System.out.println("Invalid input: service number must be exactly 5 characters.");
			serviceNum = ScannerWrapper.getInput("Enter a new service number: ");
		} // end if
		
		// Service number cannot start with 0
		while (serviceNum.charAt(0) == '0') {
			System.out.println("Invalid input: service number cannot begin with 0.");
			serviceNum = ScannerWrapper.getInput("Enter a new service number: ");
		} // end if
		
		// Ensure that service number doesn't already exist
		int serviceNumber = Integer.parseInt(serviceNum);
		if (Services.find(serviceNumber) == true) { 
			System.out.println("Service number already exists; cannot create new service.");
			return -1;
		} // end if

		return serviceNumber;
	} // send validateServiceNum method
	
	//---------------------------------------------------------------------------------------------
	
	/*
	 * VALIDATESERVICENAME: Ensures that service name is valid.
	 * CONSTRAINTS: Must be between 3-39 alphanumeric characters, not beginning or ending
	 * with a space.
	 * 
	 * Input: none
	 * Output: service name once valid
	 */
	private static String validateServiceName(String serviceName) {
		// Cannot start or end with spaces
		while (serviceName.trim().length() != serviceName.length()) {
			System.out.println("Service name cannot start or end with whitespace.");
			serviceName = ScannerWrapper.getInput("Enter a new service name: ");
		} // end while
		
		// Cannot be less than 3 characters
		while (serviceName.length() < 3) {
			System.out.println("Service name must be at least 3 characters.");
			serviceName = ScannerWrapper.getInput("Enter a new service name: ");
		} // end while
		
		// Cannot be greater than 39 characters
		while (serviceName.length() > 39) {
			System.out.println("Service name cannot be greater than 39 characters.");
			serviceName = ScannerWrapper.getInput("Enter a new service name: ");
		} // end while

		// Name is valid!
		return serviceName;
	} // end validateServiceName
	
} // end ServiceCommands class
