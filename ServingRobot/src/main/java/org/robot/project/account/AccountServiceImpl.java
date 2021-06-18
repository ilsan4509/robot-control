package org.robot.project.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AccountService")
public class AccountServiceImpl implements AccountService{
	@Autowired
	private AccountDAO accountDAO;
	
	public AccountVO getAccount(AccountVO vo) {
		return accountDAO.getAccount(vo);
	}

}
