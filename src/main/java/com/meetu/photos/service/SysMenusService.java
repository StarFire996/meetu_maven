package com.meetu.photos.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meetu.core.db.Page;
import com.meetu.core.db.Record;
import com.meetu.photos.dao.SysMenusDao;
import com.meetu.photos.domain.Citys;
import com.meetu.photos.domain.Schools;
import com.meetu.photos.domain.SysMenus;

@Service("sysmenusservice")
@Transactional
public class SysMenusService {

    @Autowired
    private SysMenusDao sysMenusDao;

    /**
     * 分页数据
     * 
     * @param pageNumber
     * @param pageSize
     * @param select
     * @param sqlExceptSelect
     * @param paras
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Record> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect,
            Object... paras) throws Exception {
        return this.sysMenusDao.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras);
    }

    public List<Map<String, Object>> findMsg(String sql, Object[] param) throws Exception {
        return this.sysMenusDao.findSQL(sql, param);
    }

    public List<Map<String, Object>> findAll(String sql) throws Exception {
        return this.sysMenusDao.findSQL(sql);
    }

    public void save(SysMenus sysMenus) {
        sysMenusDao.save("com.meetu.photos.dao.SysMenusDao.insertOper", sysMenus);
    }

    /**
     * 更新学校数据
     * 
     * @throws Exception
     * */
    @Transactional
    public void updateSchools(List<Schools> list) throws Exception {
        if (list != null && list.size() > 0) {
            sysMenusDao.updateSQL("truncate table sys_schools", new Object[] {});
            for (int i = 0; i < list.size(); i++) {
                Schools school = list.get(i);
                sysMenusDao.updateSQL(
                        "insert into sys_schools (school_id,school_name,province_id) values (?,?,?)",
                        new Object[] { school.getSchool_id(), school.getSchool_name(),
                                school.getProvince_id() });
                // System.out.println(school.getSchool_id()+"_"+school.getSchool_name()+"_"+school.getProvince_id());
            }

        }
    }

    public void updateCitys(List<Citys> list) throws Exception {
        // TODO Auto-generated method stub
        if (list != null && list.size() > 0) {
            sysMenusDao.updateSQL("truncate table sys_citys", new Object[] {});
            for (int i = 0; i < list.size(); i++) {
                Citys city = list.get(i);
                sysMenusDao
                        .updateSQL(
                                "insert into sys_citys (id,province,province_num,city,city_num,town,town_num,city_town,city_town_num) values (?,?,?,?,?,?,?,?,?)",
                                new Object[] { city.getId(), city.getProvince(), city.getProvince_num(),
                                        city.getCity(), city.getCity_num(), city.getTown(),
                                        city.getTown_num(), city.getCity_town(), city.getCity_town_num() });
                // System.out.println(city.getCity_town_num()+"_"+city.getCity_town()+"_"+city.getId());
            }

        }
    }

}
