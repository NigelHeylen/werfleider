package nigel.com.werfleider.dao.contact;

import java.util.List;

import nigel.com.werfleider.model.Contact;
import nigel.com.werfleider.model.Profession;

/**
 * Created by nigel on 11/02/15.
 */
public interface ContactDbHelper {


    /*
 * Creating a contact
 */
    long createContact(Contact contact);

    /*
         * get single contact
         */
    Contact getContact(String email);

    /*
         * getting all contacts by profession
         * */
    List<Contact> getAllContactsByProfession(Profession profession);

    /*
     * Updating a contact
     */
    int updateContact(Contact contact);

    // closing database
    void closeDB();

    void deleteContact(Contact contact);

    int getContactCount(Profession profession);
}
