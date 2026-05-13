package com.emile.fintech.user.repos;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.transaction.annotation.Transactional;

import com.emile.fintech.user.model.User;

//This interface is to perform CRUD operations: create, read, update, and delete.
public interface UserDataRepository extends JpaRepository<User, Long> /* The first data type in the angle brackets defines for which entity will the CRUD 
																	   * operations be performed. The second data type identifies the data type of entity's 
																	   * instance variable marked w/ @Id. */
{
	@Query(value = "SELECT id FROM User WHERE username=:username") // binds a JPQL/nativeQuery query to a method in the repository interface.
	Long findIdByUsername(@Param("username") String username); // @Param binds a method parameter to a named query parameter.
	
	User findByUsername(String username);
	
//	@Query(value = "SELECT password FROM User WHERE id=:id")
//	String findPasswordById(@Param("id") int id);
//	
//	@Transactional
//	@Query(value = "UPDATE User SET password=:password WHERE id=:id")
//	@Modifying
//	int updatePasswordById(@Param("id") int id, @Param("password") String password);
}
