package test.com.github.mybatis.ext.dao;

import com.github.mybatis.ext.AbstractDao;
import com.github.mybatis.ext.page.PageParam;
import org.apache.ibatis.annotations.Select;
import test.com.github.mybatis.ext.model.User;

import java.util.List;

/**
 * @author xiebo
 */
public interface UserDao extends AbstractDao {

    @Select("select * from users")
    public List<User> findList(PageParam pageParam);

}
