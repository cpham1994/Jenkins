package com.revature.data;

import java.util.List;

import com.revature.beans.Account;

public interface AccountDao {
	Account getAccount(int id);
	List<Account> getAccounts();
	void getAllAccounts();
	void saveAccount(Account a);
	void updateAccount(Account a);
	void deleteAccount(Account a);
}
