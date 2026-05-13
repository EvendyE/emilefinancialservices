package com.emile.fintech.transactions.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emile.fintech.accounts.model.AccountsId;
import com.emile.fintech.transactions.model.Transactions;

//This interface is to perform CRUD operations: create, read, update, and delete.
public interface TransactionsDataRepository extends JpaRepository<Transactions, AccountsId> /* The first data type in the angle brackets defines for which entity will the CRUD 
																							   operations be performed. The second data type identifies the data type of entity's 
																							   instance variable marked w/ @Id. */
{
}
