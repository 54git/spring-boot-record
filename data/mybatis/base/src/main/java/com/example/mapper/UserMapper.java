package com.example.mapper;

import com.example.mapper.provider.UserProvider;
import com.example.model.Inner;
import com.example.model.Tree;
import com.example.model.User;
import com.example.type.State;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author 李磊
 * @datatime 2020-01-16
 * @description mapper
 */
// 继承CoreMapper 同时也继承了CoreMapper对应xml的配置
public interface UserMapper/* extends CoreMapper*/ {

    // 相当于mapper.xml中的useGeneratedKeys="true" keyColumn="id" keyProperty="id"
    // @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    // 可以获取所有数据类型的id
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    /**
     * @SelectProvide
     * 指定的Class 必须能通过无参构造初始化
     * 指定方法 必须为public 返回值必须为String
     */
    @InsertProvider(type = UserProvider.class, method = "insert")
    // @Insert("INSERT INTO user (user_name, password, age, mail, birthday) " +
    //         "VALUES (#{userName}, #{password}, #{age}, #{mail}, #{birthday})")
    int insert(User user);

    @Insert("<script>" +
            "INSERT INTO user (user_name, password, age, mail, birthday) " +
            "VALUE" +
            "<foreach collection='array' item='item' separator=','>" +
            "(#{item.userName}, #{item.password}, #{item.age}, #{item.mail}, #{item.birthday})" +
            "</foreach>" +
            "</script>")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int inserts(User[] users);

    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteById(Long id);

    // 可使用Optional接收
    @ResultMap("user")
    @Select("SELECT user_name, password, age, mail, birthday FROM user WHERE id = #{id}")
    Optional<User> selectById(Long id);

    // @ResultMap("com.example.mapper.CoreMapper.user")

    /*
     * @Results
     *   id 当前类中或当前对应xml其它方法可使用@ResultMap("id")使用此映射
     *   但其它类无法通过namespace引用 xml方式还可通过namespace引用
     * @Result
     *   映射字段名称和类型
     * 无法定义一对多 一对一字段对应 只能通过指定其它方法实现
     */
    @Results(id = "user", value = {
            // 相同名称 可省略书写映射
            @Result(column = "user_name", property = "userName")
            // , @Result(id = true, column = "id", property = "id")
            // , @Result(column = "age", property = "age")
    })
    @Select("SELECT id, user_name, password, age FROM user")
    List<User> findAll(User user);

    @MapKey("id")
    Map<Long, User> findMap(Map params);

    /**
     * java映射mysql数字 负数和0为false 正数为true
     * 非数字类型字符串直接报错
     */
    @Select("SELECT #{value}")
    boolean booleanTest(String value);

    /**
     * 当字符串不为空串时 且判断具体值时需使用双引号包含
     */
    @Select("<script>" +
            "<if test=\"e == ''\">" +
            "SELECT 0" +
            "</if>" +
            "<if test='e == \"1\"'>" +
            "SELECT 1" +
            "</if>" +
            "</script>")
    String ifTest(String content);

    /**
     * 当foreach标签本次循环中无数据时 不会拼接separator
     * separator默认值为空字符串
     * 数组或集合需要使用@Param标识 单个实体类参数不需要标识
     * 数组可使用`array`作为变量名称
     * list可使用`list`作为变量名称
     */
    @Select("<script>" +
            "SELECT CONCAT(" +
            "<foreach collection='array' item='value' index='index' separator=','>" +
            "<if test=\"value != null and value != ''\">" +
            "#{value}" +
            "</if>" +
            "</foreach>" +
            ")" +
            "</script>")
    String arrayTest(/*@Param("values") */String[] values);

    @Select("SELECT id, parent_id AS parentId, name FROM tree")
    List<Tree> tree();

    List<Tree> dbTree(Long parentId);

    @Insert("INSERT INTO user (is_del) VALUES (#{state})")
    int insertEnum(State state);

    @Select("SELECT is_del FROM user LIMIT 1")
    State findEnum();

    List<Map> findPage();

    // https://commons.apache.org/proper/commons-ognl/language-guide.html
    // 调用java类方法 需使用`$`包含
    // @Select("SELECT '${@com.example.util.StringUtil@uuid()}'")
    // @Select("SELECT ${@com.example.util.StringUtil@uuid().hashCode()}")
    // @Select("SELECT '${@com.example.util.DateUtil@FORMAT}'")
    @Select("SELECT '${'李磊'.hashCode()}'")
    String run();

    @Select("<script>" +
            "SELECT CONCAT(#{content} ," +
            "<foreach collection='inner1s' item='item1' separator=','>" +
            "   #{item1.content} ," +
            "   <foreach collection='item1.inner2s' item='item2' separator=','>" +
            "       #{item2.content}" +
            "   </foreach>" +
            "</foreach> ," +
            "<foreach collection='inner2s' item='item2' separator=','>" +
            "   #{item2.content}" +
            "</foreach>" +
            ")" +
            "</script>")
    String inner(Inner inner);
}