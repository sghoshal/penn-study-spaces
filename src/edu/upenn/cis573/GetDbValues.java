package edu.upenn.cis573;

public class GetDbValues{
	 
    //private variables
    String _name1;
    String _name2;
    String _phone;
    String _email;
      
//    // constructor
    public GetDbValues(String name1, String name2, String phone, String email){
        
    	this._name1 = name1;
        this._name2 = name2;
        this._phone = phone;
        this._email = email;
        
    }
 
    
    // getting name
    public String getname1(){
        return this._name1;
    }
 
    // setting name
    public void setname1(String name1){
        this._name1 = name1;
    }
    
    // getting name
    public String getphone(){
        return this._phone;
    }
 
    // setting name
    public void setphone(String phone){
        this._phone = phone;
    }
    
    // getting name
    public String getname2(){
        return this._name2;
    }
 
    // setting name
    public void setname2(String name2){
        this._name2 = name2;
    }
    
    // getting name
    public String getemail(){
        return this._email;
    }
 
    // setting name
    public void setemail(String email){
        this._email = email;
    }
 
}
