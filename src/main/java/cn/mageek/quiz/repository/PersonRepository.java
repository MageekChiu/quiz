package cn.mageek.quiz.repository;

import cn.mageek.quiz.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PersonRepository extends JpaRepository<Person, Integer> {
    //方法命名查询
    /**
    * 通过地址进行查询，参数为address,
    * 相当于JPQL：select p from Person p where p.address=?1
    */
    List<Person> findByAddress(String address);
    //方法命名查询
    /**
     * 通过地址和名字进行查询，参数为name,address
     * 相当于JPQL：select p from Person p where p.name=?1 and address=?2
     * */
    Person findByNameAndAddress(String name, String address);

    //Query查询
    /**
     * @param name
     * @param address
     * @return
     */
    @Query("select p from Person p where p.name=?1 and p.address=?2")
    Person withNameAndAddressQuery(String name, String address);

    /**
     * 更新
     * @param name
     * @return
     */
    @Modifying
    @Query("update Person p set p.name=?1 ")
    int setName(String name);
}
