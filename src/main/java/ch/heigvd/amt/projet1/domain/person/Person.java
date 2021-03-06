package ch.heigvd.amt.projet1.domain.person;

import ch.heigvd.amt.projet1.domain.IEntity;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
// public pour effectuer les tests
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Person implements IEntity<Person,PersonId> {

    private PersonId id;
    private String username;
    private String email;
    private String firstname;
    private String lastName;
    @EqualsAndHashCode.Exclude
    private String hashedPassword;

    @Override
    public Person deepClone() {
        return this.toBuilder().id(new PersonId(id.asString())).build();
    }

    public boolean authenticate(String clearTextPassword){
        return  BCrypt.checkpw(clearTextPassword, hashedPassword);
    }

    public static class PersonBuilder{
        public PersonBuilder clearTextPassword(String clearTextPassword){
            if(clearTextPassword == null || clearTextPassword.isEmpty()){
                throw new IllegalArgumentException("Password is mandatory");
            }
            hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
            return this;
        }

        public Person build(){
            if(id==null){
                id = new PersonId();
            }
            if(username==null || username.isEmpty())
                throw new IllegalArgumentException("Username is mandatory");
            if(hashedPassword==null || hashedPassword.isEmpty())
                throw new IllegalArgumentException("Password is mandatory");
            if(email==null || email.isEmpty())
                throw new IllegalArgumentException("Email is mandatory");
            if(firstname==null ||firstname.isEmpty())
                throw new IllegalArgumentException("Firstname is mandatory");
            if(lastName==null || lastName.isEmpty())
                throw new IllegalArgumentException("Lastname is mandatory");
            return new Person(id,username,email,firstname,lastName,hashedPassword);
        }
    }
}
