/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pts4.chatserver;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Singleton klasse die ervoor zorgt dat er een nieuwe unit kan worden geregistreerd.
 * @author Leo
 */
public class RegisterUnitsController 
{
    private static RegisterUnitsController controller = null;
    private RegisterUnitsController()
    {
        
    }
    
    public boolean registerNewUnit(int idpersonal_type, String first_name, String last_name, String middle_name, String username, String password, String SSN, String email, Date Birthdate, String phonenumber, String Street, String City, String Postal, String Region)
    {
        if(idpersonal_type > 3 || idpersonal_type < 1)
        {
            return false;
        }
        if(!email.contains("@"))
        {
            return false;
        }
        if(Pattern.matches("[a-zA-Z]+", phonenumber) == true || phonenumber.length() < 10)
        {
            return false;
        }
        if(Pattern.matches("[a-zA-Z]+", Postal.substring(0, 4)) == true || Pattern.matches("[a-zA-Z]+", Postal.substring(5, 2)) == false)
        {
            return false;
        }
//        if(database.insertEmergencyService(idpersonal_type, first_name, last_name, middle_name, username, password, SSN, email, Birthdate, phonenumber, Street, City, Postal, Region) == true)
//        {
//            return true;
//        }
        return false;
    }
    
    public static RegisterUnitsController getInstance()
    {
        if(controller == null)
        {
            controller = new RegisterUnitsController();
        }
        return controller;
    }
}
