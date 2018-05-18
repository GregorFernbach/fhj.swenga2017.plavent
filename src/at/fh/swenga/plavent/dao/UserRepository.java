package at.fh.swenga.plavent.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import at.fh.swenga.plavent.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findFirstByUsername(String username);

	@Query("Select u from User u")
	public List<User> findAllBy();
	
	@Query("Delete from User u where u.username = :username")
	public void deleteByUserName(@Param ("username") String username);
	
	@Query(value = "SELECT u.* " + 
				   	"FROM User u " +
					"WHERE u.username != (SELECT h.username " + 
					"					 FROM Happening h " + 
					"                     WHERE h.happeningId = :happeningID) " + 
					"  AND u.username NOT IN (SELECT g.username " +
					"						 FROM Guestlist g " +
					"                         WHERE g.happeningId = :happeningID)", nativeQuery = true)
	public List<User> getPotentialGuestsForHappening(@Param("happeningID") int happeningId);
	
	@Query("Select u From User u where LOWER(u.username) LIKE LOWER(CONCAT('%',:username ,'%'))" )
	public List<User> getFilteredUsers(@Param("username") String username);
}
