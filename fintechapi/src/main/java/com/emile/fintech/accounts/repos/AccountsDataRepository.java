package com.emile.fintech.accounts.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emile.fintech.accounts.model.Accounts;
import com.emile.fintech.accounts.model.AccountsId;

//This interface is to perform CRUD operations: create, read, update, and delete.
public interface AccountsDataRepository extends JpaRepository<Accounts, AccountsId> /* The first data type in the angle brackets defines for which entity will the CRUD 
																					operations be performed. The second data type identifies the data type of entity's 
																					instance variable marked w/ @Id. */
{
	@Query(value = "SELECT balance, account_type, account_number FROM emile_bank_accounts.Accounts WHERE id=:id", nativeQuery = true) // binds a JPQL/nativeQuery query to a method in the repository interface.
	List<Object> findByAccountId(@Param("id") Long id); // @Param binds a method parameter to a named query parameter.
}
