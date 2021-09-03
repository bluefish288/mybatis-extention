package test.com.github.mybatis.ext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mybatis.ext.SqlSessionFactoryUtil;
import com.github.mybatis.ext.model.InsertModel;
import com.github.mybatis.ext.model.UpdateModel;
import com.github.mybatis.ext.page.Page;
import com.github.mybatis.ext.page.PageParam;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import test.com.github.mybatis.ext.dao.UserDao;
import test.com.github.mybatis.ext.model.State;
import test.com.github.mybatis.ext.model.User;

/**
 * @author xiebo
 */
public class MainTest {

    private static  SqlSession session;

    @BeforeClass
    public static void setup(){
        SqlSessionFactory sqlSessionFactory = Util.getSqlSessionFactory();
        SqlSessionFactoryUtil.fill(sqlSessionFactory);

        session = sqlSessionFactory.openSession(true);
    }

    @AfterClass
    public static void tearDown(){
        if(null!=session){
            session.close();
        }
    }

    @Test
    public void testSave(){
        UserDao userDao = session.getMapper(UserDao.class);

        User user1 = new User();
        user1.setName("测试");
        user1.setState(State.ALLOW);
        userDao.insertByModel(InsertModel.build(user1));

        System.out.println(user1.getTestId());

    }

    @Test
    public void testUpdate(){
        UserDao userDao = session.getMapper(UserDao.class);

        User user = new User();
        user.setTestId(23575);
        user.setName("测试2");

        System.out.println(userDao.updateByModel(UpdateModel.build(user)));
    }

    @Test
    public void testFindById(){

        UserDao userDao = session.getMapper(UserDao.class);

        User user = userDao.findModelById(User.class, 23575);

        try {
            System.out.println(new ObjectMapper().writeValueAsString(user));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindPage(){

        UserDao userDao = session.getMapper(UserDao.class);

        Page<User> page = userDao.findPage(new PageParam(), userDao::findList);

        System.out.println(page);

    }


}
