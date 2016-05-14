package com.meetu.util;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.meetu.biu.dao.BiuDao;
import com.meetu.biu.domain.Biu;
import com.meetu.dao.UserDao;
import com.meetu.grabbiu.dao.GrabBiuDao;

@Controller
@RequestMapping("sqltest")
public class SqlTestController {
	
	@Autowired
	private GrabBiuDao grabBiuDao;
	
	@Autowired
	private BiuDao biuDao;
	
	@Autowired
	private UserDao userDao;
	
	
	
	@RequestMapping(value = "insertBiu", method = RequestMethod.POST)
	public ResponseEntity<Void> insert() {
		Biu biu = new Biu();
		biu.setAccept_num(0);
		biu.setAgree_biu(1);
		biu.setChat_tags("");
		biu.setCreated_by(UUID.randomUUID().toString());
		biu.setId(UUID.randomUUID().toString());
		biu.setLast_date(new Date());
		biu.setStatus(0);
		this.biuDao.insertOper(biu);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "updateBiu", method = RequestMethod.POST)
	public ResponseEntity<Void> update(@RequestParam("id") String id,
			@RequestParam("chat_tags") String chat_tags,
			@RequestParam("status") Integer status) {
		Biu biu = this.biuDao.selectBiuById(id);
		biu.setChat_tags(chat_tags);
		biu.setStatus(status);
		this.biuDao.updateBiu(biu);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "selectBiu", method = RequestMethod.POST)
	public ResponseEntity<Void> select(@RequestParam("id") String id) {
		Biu biu = this.biuDao.selectBiuById(id);
		System.out.println(biu);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "insertAll", method = RequestMethod.POST)
	public ResponseEntity<Void> insertAll() {
		List<String> ids = userDao.selectAllUserId();

		if (ids != null && ids.size() > 0) {
			for (String userId : ids) {
				Biu biu = new Biu();
				biu.setAccept_num(0);
				biu.setAgree_biu(1);
				biu.setChat_tags("");
				biu.setCreated_by(userId);
				biu.setId(Common.generateId());
				biu.setLast_date(new Date());
				biu.setStatus(1);
				this.biuDao.insertOper(biu);
			}
		}

		return ResponseEntity.ok().build();
	}
}
