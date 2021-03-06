/*
 * ServiceNumber
 * object to hold the service number information for the system
 * Spice Tests
 * 13/10/2018
 */

public class ServiceNumber {
	
	int number;
	
	//---------------------------------------------------------------------------------------------
	
	/*
	 * ServiceNumber constructor.
	 */
	public ServiceNumber(int number) {
		this.number = number;
	} // end ServiceNumber constructor
	
	//---------------------------------------------------------------------------------------------
	
	/*
	 * GETNUMBER: Returns service number as int
	 * 
	 * Input: none
	 * Output: service number
	 */
	public int getNumber() {
		return number;
	} // end getNumber method

} // end ServiceNumber class
