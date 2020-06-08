package com.yat.mybatis_plus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yat.MybatisPlusApplication;
import com.yat.modules.entity.UserEntity;
import com.yat.modules.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * <p>Description: 描述 </p>
 *
 * @author Yat
 * @Created with IDEA
 * @Date: 2020/5/20
 * @Time: 10:45
 */
@RunWith(SpringRunner.class)
@RequiredArgsConstructor
@SpringBootTest(classes = MybatisPlusApplication.class)
public class SqlTest {

    private final IUserService userService;

    /**
     * 插入测试数据
     */
    @Test
    public void saveTestData() {
        for (int i = 1; i < 2; i++) {
            UserEntity userEntity = UserEntity.builder()
                    .name("name:" + i)
                    .age(i)
                    .email("email:" + i)
                    .updateTime(new Date())
                    .build();
            userService.save(userEntity);
            System.out.println(userEntity);
        }

    }

    /**
     * 插入测试数据：数据不存在就新增，存在就修改
     */
    @Test
    public void saveOrUpdateTestData() {
        for (int i = 1; i < 10; i++) {
            UserEntity userEntity = UserEntity.builder()
                    .id(1262946394988118017L)
                    .name("name:" + i)
                    .age(i)
                    .email("email:" + i)
                    .build();
            userService.saveOrUpdate(userEntity);
            System.out.println(userEntity);
        }

    }

    /**
     * 查询所有
     */
    @Test
    public void queryAll() {
        List<UserEntity> userEntities = userService.list(null);
        userEntities.forEach(System.out::println);
    }

    @Test
    public void updateData() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1262941647992262658L);
        userEntity.setName("name:11");
        userEntity.setEmail("email:11");
        userService.updateById(userEntity);
        System.out.println(userEntity);
    }

    /**
     * 测试 乐观锁插件
     */
    @Test
    public void testOptimisticLocker() {
        //查询
        UserEntity user = userService.getById(1263032674497355777L);

        //修改数据
        user.setName("Helen Yao");
        user.setEmail("helen@qq.com");

        //执行更新
        userService.updateById(user);
    }

    /**
     * 测试乐观锁插件 失败
     */
    @Test
    public void testOptimisticLockerFail() {
        //查询
        UserEntity user = userService.getById(1263032674497355777L);

        //修改数据
        user.setName("Helen Yao1");
        user.setEmail("helen@qq.com1");

        // 模拟取出数据后，数据库中version实际数据比取出的值大，即已被其它线程修改并更新了version
        user.setVersion(user.getVersion() - 1);

        //执行更新
        userService.updateById(user);
    }

    /**
     * 简单的条件查询
     */
    @Test
    public void testSelectByMap() {
        List<UserEntity> list = userService.list(
                new QueryWrapper<UserEntity>().lambda()
                        .eq(UserEntity::getName, "name:2")
                        .eq(UserEntity::getEmail, "email:2")
        );
        list.forEach(System.out::println);
    }

    /**
     * 测试page分页
     */
    @Test
    public void testSelectPage() {
        Page<UserEntity> page = new Page<>();
        userService.page(page, null);
        page.getRecords().forEach(System.out::println);
        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }

    /**
     * 测试自定义分页
     */
    @Test
    public void testSelectMapsPage() {

        Page<UserEntity> page = new Page<>(1, 5);
        IPage<UserEntity> mapIPage = userService.selectUserPage(page, 1);

        //注意：此行必须使用 mapIPage 获取记录列表，否则会有数据类型转换错误
        mapIPage.getRecords().forEach(System.out::println);

        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }

    /**
     * 删除全部数据
     */
    @Test
    public void delData() {
        boolean remove = userService.remove(null);
        System.out.println(remove);
    }

    /**
     * 根据id删除记录
     */
    @Test
    public void testDeleteById() {
        boolean removeById = userService.removeById(1263032674497355777L);
        System.out.println(removeById);
    }

    /**
     * 批量删除
     */
    @Test
    public void testDeleteBatchIds() {
        boolean result = userService.removeByIds(
                Arrays.asList(1262946395957002242L, 1262946396258992130L, 1262946622449401858L)
        );
        System.out.println(result);
    }

    /**
     * 简单的条件查询删除
     */
    @Test
    public void testDeleteByMap() {
        boolean result = userService.remove(
                new LambdaQueryWrapper<UserEntity>()
                        .eq(UserEntity::getName, "name:5")
                        .eq(UserEntity::getAge, 5)
        );
        System.out.println(result);
    }

    /**
     * ge、gt、le、lt、isNull、isNotNull
     */
    @Test
    public void testDelete() {
        // 因为配置了逻辑删除，所以这里是修改 SQL：UPDATE user SET deleted =-1 WHERE deleted=0 AND name IS NULL AND age >= ? AND email IS NOT NULL
        boolean remove = userService.remove(
                new LambdaQueryWrapper<UserEntity>()
                        .isNull(UserEntity::getName)
                        .ge(UserEntity::getAge, 12)
                        .isNotNull(UserEntity::getEmail)
        );
        System.out.println(remove);
    }


    /**
     * eq、ne
     * <p>getOne:方法的第二个参数表示，如果查询出来的数据为多条，是否抛出异常，默认为false，不抛出异常</p>
     */
    @Test
    public void testSelectOne() {
        // SQL：SELECT id,name,age,email,create_time,update_time,version,deleted FROM user WHERE deleted=0 AND name <> ?
        UserEntity user = userService.getOne(
                new LambdaQueryWrapper<UserEntity>()
                        .ne(UserEntity::getName, "Tom")
                , false);
        System.out.println(user);
    }

    /**
     * 包含大小边界
     * between、notBetween
     */
    @Test
    public void testSelectCount() {
        Integer count = userService.count(
                new LambdaQueryWrapper<UserEntity>()
                        .between(UserEntity::getAge, 1, 30)
        );
        System.out.println(count);
    }

    /**
     * 全部的 ‘等于’ 条件：allEq
     */
    @Test
    public void testSelectList() {

        // SQL： SELECT id,name,age,email,create_time,update_time,version,deleted FROM user WHERE deleted=0 AND name = ? AND id = ? AND age = ?
        Map<String, Object> map = new HashMap<>();
        map.put("id", 2);
        map.put("name", "Jack");
        map.put("age", 20);

        List<UserEntity> users = userService.list(
                new QueryWrapper<UserEntity>()
                        .allEq(map)
        );
        users.forEach(System.out::println);
    }


    /**
     * 模糊查询：like - LIKE '%e%' 、notLike - NOT LIKE '%e%' 、likeLeft - LIKE '%t'、likeRight - LIKE 't%'
     */
    @Test
    public void testSelectMaps() {
        //返回值是Map列表
        List<Map<String, Object>> maps = userService.listMaps(
                new LambdaQueryWrapper<UserEntity>()
                        .notLike(UserEntity::getName, "e")
                        .likeRight(UserEntity::getEmail, "t")
        );
        maps.forEach(System.out::println);
    }

    /**
     * <p>in、notIn、inSql、notinSql、exists、notExists</p>
     * <p>inSql、notinSql：可以实现子查询</p>
     */
    @Test
    public void testSelectObjs() {
        List<Object> objects = userService.listObjs(
                new QueryWrapper<UserEntity>()
                        .inSql("id", "select id from user where id < 3")
                // .in("id", 1, 2, 3)
        );
        objects.forEach(System.out::println);
    }

    /**
     * or、and
     */
    @Test
    public void testUpdate1() {
        //修改值
        UserEntity user = new UserEntity();
        user.setAge(99);
        user.setName("Andy");
        //修改条件
        UpdateWrapper<UserEntity> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .like("name", "h")
                .or()
                .between("age", 20, 30);
        boolean result = userService.update(user, userUpdateWrapper);
        System.out.println(result);
    }


    /**
     * 嵌套or、嵌套and
     */
    @Test
    public void testUpdate2() {
        // SQL: UPDATE user SET name=?, age=?, update_time=? WHERE deleted=0 AND name LIKE ? OR ( name = ? AND age <> ? )
        boolean result = userService.update(
                UserEntity.builder()
                        .age(99)
                        .name("Andy")
                        .build(),
                new UpdateWrapper<UserEntity>().lambda()
                        .like(UserEntity::getName, "h")
                        .or(i -> i.eq(UserEntity::getName, "李白").ne(UserEntity::getAge, 20))
        );
        System.out.println(result);
    }

    /**
     * 排序：orderBy、orderByDesc、orderByAsc
     */
    @Test
    public void testSelectListOrderBy() {
        List<UserEntity> users = userService.list(
                new LambdaQueryWrapper<UserEntity>()
                        .orderByDesc(UserEntity::getId)
        );
        users.forEach(System.out::println);
    }

    /**
     * <p> last:直接拼接到 sql 的最后 </p>
     * <p style="color:red">只能调用一次,多次调用以最后一次为准 有sql注入的风险,请谨慎使用 </p>
     */
    @Test
    public void testSelectListLast() {
        List<UserEntity> users = userService.list(
                new QueryWrapper<UserEntity>()
                        .last("limit 1")
        );
        users.forEach(System.out::println);
    }

    /**
     * 指定要查询的列
     */

    @Test
    public void testSelectListColumn() {
        List<UserEntity> users = userService.list(
                new LambdaQueryWrapper<UserEntity>()
                        .select(UserEntity::getId, UserEntity::getName, UserEntity::getAge)
        );
        users.forEach(System.out::println);
    }

    /**
     * 指定要修改的列: set、setSql
     */
    @Test
    public void testUpdateSet() {
        //修改值
        UserEntity user = new UserEntity();
        user.setAge(99);
        String email = "123@qq.com";
        boolean result = userService.update(user,
                new UpdateWrapper<UserEntity>()
                        .like("name", "h")
                        // 除了可以查询还可以使用set设置修改的字段
                        .set("name", "老李头")
                        // 直接写SQL
                        // .setSql(" email = '123@qq.com'")
                        // 动态赋值时，String类型需要拼接 单引号：'' ,否则会报错
                        .setSql(" email = '" + email + "'")

        );
        System.out.println(result);
    }
}
