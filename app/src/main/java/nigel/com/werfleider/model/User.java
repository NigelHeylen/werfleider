package nigel.com.werfleider.model;

import com.parse.ParseUser;

import static nigel.com.werfleider.util.ParseStringUtils.COMPANY;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static nigel.com.werfleider.util.ParseStringUtils.PROFESSION;

/**
 * Created by nigel on 01/04/15.
 */
public class User extends ParseUser {

    public String getProfession(){
        return getString(PROFESSION);
    }

    public void setProfession(final String profession){
        put(PROFESSION, profession);
    }

    public String getCompany(){
        return getString(COMPANY);
    }

    public void setCompany(final String company){
    }

    public void setName(final String name) {
        put(NAME, name);
    }

    public String getName(){
        return getString(NAME);
    }

    public static User getCurrentUser(){
        return (User) ParseUser.getCurrentUser();
    }

}
