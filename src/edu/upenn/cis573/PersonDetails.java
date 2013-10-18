package edu.upenn.cis573;

public class PersonDetails {
	 
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    /**
     * Constructor
     * @param name1
     * @param name2
     * @param phone
     * @param email
     */
    public PersonDetails (String firstName, String lastName, String phone, String email){
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.phone = phone;
    	this.email = email;
    }

    /* Getters and Setters */
    
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
 
	public String getUniqueId () {
		return (String.format("%s%s", firstName, phone));
	}
	
	@Override
	public String toString() {
		return (String.format("%s %s", firstName, lastName));
	}
}
