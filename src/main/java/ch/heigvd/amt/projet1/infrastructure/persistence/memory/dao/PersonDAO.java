package ch.heigvd.amt.projet1.infrastructure.persistence.memory.dao;

import ch.heigvd.amt.projet1.domain.person.Person;
import ch.heigvd.amt.projet1.domain.person.PersonId;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class PersonDAO implements PersonDAOLocal {

    @Resource(lookup = "jdbc/AMTDS")
    private DataSource dataSource;

    public long save(Person person) {
        /*
        This function aims to insert in the database a Person
        */
        try{
            Connection con = dataSource.getConnection();

            String uuid = person.getId().asString();
            //uuid = uuid.substring(uuid.lastIndexOf("@"+1));

            PreparedStatement ps = con.prepareStatement("INSERT INTO Person (id, username, email, firstname, lastname, password) VALUES('"+
                    uuid+"','"+
                    person.getUsername()+"','"+
                    person.getEmail()+"','"+
                    person.getFirstname()+"','"+
                    person.getLastName()+"','"+
                    person.getHashedPassword()+
                    "')");
            boolean rs = ps.execute();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    // TODO: NE SAIT PAS SI FONCTIONNE
    public long remove(PersonId id) {
        /*
        This function aims to remove an user by its id
         */
        try{
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("DELETE FROM Person WHERE id = " + id.asString());
            ResultSet rs = ps.executeQuery();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    // TODO: NE SAIT PAS SI FONCTIONNE
    public long removeByUsername(String username) {
        /*
        This function aims to remove an user by its username
         */
        try{
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("DELETE FROM Person WHERE username LIKE '" + username + "'");
            ResultSet rs = ps.executeQuery();

            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    // TODO: NE SAIT PAS SI FONCTIONNE
    public Person findById(PersonId id) {
        /*
        This function aims to find and return an user by its id
         */
        Person result = null;
        try{
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM Person WHERE id = " + id.asString());
            ResultSet rs = ps.executeQuery();

            result = Person.builder()
                    .id(new PersonId(rs.getString("id")))
                    .username(rs.getString("username"))
                    .firstname(rs.getString("firstname"))
                    .lastName(rs.getString("lastname"))
                    .email(rs.getString("email"))
                    .hashedPassword(rs.getString("password"))
                    .build();

            ps.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(PersonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public Person findByUsername(String username) {
        /*
        This function aims to find and return an user by its id
         */
        Person result = null;
        try{

            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM Person WHERE username = '" + username + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Person newPerson = Person.builder()
                        .id(new PersonId(rs.getString("id")))
                        .username(rs.getString("username"))
                        .firstname(rs.getString("firstname"))
                        .lastName(rs.getString("lastname"))
                        .email(rs.getString("email"))
                        .hashedPassword(rs.getString("password"))
                        .build();
                result = newPerson;
            }
            ps.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(PersonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    // TODO: NE SAIT PAS SI FONCTIONNE
    public List<Person> findAll() {
        List<Person> result = new LinkedList<Person>();
        try{
            Connection con = dataSource.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM Person");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Person newPerson = Person.builder()
                        .id(new PersonId(rs.getString("id")))
                        .username(rs.getString("username"))
                        .firstname(rs.getString("firstname"))
                        .lastName(rs.getString("lastname"))
                        .email(rs.getString("email"))
                        .hashedPassword(rs.getString("password"))
                        .build();
                result.add(newPerson);
            }

            ps.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(PersonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}