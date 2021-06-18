package org.robot.project.account;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("accountDAO")
public class AccountDAO {
	@Autowired
	SqlSessionTemplate mySQL;
	
	private String loc = "org.robot.project.AccountMapper.";
	
	public AccountVO getAccount(AccountVO vo) {
		return mySQL.selectOne(loc + "getAccount", vo);
	}
	
}
